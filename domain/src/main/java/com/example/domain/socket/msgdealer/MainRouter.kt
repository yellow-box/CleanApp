package com.example.domain.socket.msgdealer

import com.example.domain.ApiService
import com.example.domain.base.GsonUtil
import com.example.domain.logic.chat.IChatRoomRepository
import com.example.domain.logic.chat.RoomMsg
import com.example.domain.socket.Executor
import com.example.domain.socket.ILogicAction
import com.example.domain.socket.ISocketMsgDealer
import com.example.domain.socket.OpConst
import com.example.domain.socket.RawDataOperator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharedFlow
import java.util.Timer
import java.util.TimerTask

/**
 * 根据 opTyoe消息类型，分发给不同 dealer 以及消息 发送回调的处理
 */
class MainRouter {

    private val msgDealerS: MutableList<ISocketMsgDealer> = ArrayList()
    private val seqCallbackMap: MutableMap<Long, ILogicAction.SeqCallback> = HashMap()
    val roomPushMsgSharedFlow: SharedFlow<RoomMsg>?
        get() {
            val dealer = msgDealerS.firstOrNull {
                it is PushMsgDealer
            } ?: return null
            return (dealer as PushMsgDealer).roomPushMsgFlow
        }

    //防止一直收不到响应时，Callback会常驻内存
    private val SEQCALLBACK_TIMEOUT_TIME = 2000L

    init {
        msgDealerS.add(BindUserDealer())
        msgDealerS.add(PushMsgDealer())
        msgDealerS.add(SendAllRspDealer())
    }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { MainRouter() }
    }

    fun putCallback(seq: Long, callback: ILogicAction.SeqCallback) {
        seqCallbackMap[seq] = callback
        //超时删除seq
        ApiService[Executor::class.java].runInMain(
            { seqCallbackMap.remove(seq) },
            SEQCALLBACK_TIMEOUT_TIME
        )
    }

    fun dealData(byteArray: ByteArray) {
        val rawDataOperator = ApiService[RawDataOperator::class.java]
        val parseResult = rawDataOperator.parseRawData(byteArray)
        println(
            "client receive from server seq:${parseResult.seq},type:${parseResult.opType},content:${
                String(
                    parseResult.dataContent
                )
            }"
        )
        if (parseResult.opType != OpConst.OP_PUSH_MESSAGE) {
            val rsp = GsonUtil.gson.fromJson(String(parseResult.dataContent), Response::class.java)
            val callback = seqCallbackMap[parseResult.seq]
            seqCallbackMap.remove(parseResult.seq)
            if (rsp.code == 200) {
                callback?.onSuccess()
            } else {
                callback?.onFail(rsp.code, rsp.msg)
            }
        }
        msgDealerS.forEach {
            if (it.matchType(parseResult.opType)) {
                it.dealData(parseResult.dataContent)
            }
        }
    }

}