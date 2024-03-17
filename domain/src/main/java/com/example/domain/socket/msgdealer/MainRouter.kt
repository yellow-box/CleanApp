package com.example.domain.socket.msgdealer

import com.example.domain.ApiService
import com.example.domain.base.GsonUtil
import com.example.domain.logic.chat.IChatRoomRepository
import com.example.domain.socket.ILogicAction
import com.example.domain.socket.ISocketMsgDealer
import com.example.domain.socket.OpConst
import com.example.domain.socket.RawDataOperator
import java.util.Timer
import java.util.TimerTask

class MainRouter {

    private val msgDealerS: MutableList<ISocketMsgDealer> = ArrayList()
    private val seqCallbackMap: MutableMap<Long, ILogicAction.SeqCallback> = HashMap()

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
//        Timer().schedule(object : TimerTask() {
//            override fun run() {
//                seqCallbackMap.remove(seq)
//            }
//        }, 2000L)
        var a = 1
    }

    fun dealData(byteArray: ByteArray) {
        val rawDataOperator = ApiService[RawDataOperator::class.java] ?: return
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
            if (rsp.code == 200) {
                seqCallbackMap[parseResult.seq]?.onSuccess()
            } else {
                seqCallbackMap[parseResult.seq]?.onFail(rsp.code, rsp.msg)
            }
        }
        msgDealerS.forEach {
            if (it.matchType(parseResult.opType)) {
                it.dealData(parseResult.dataContent)
            }
        }
    }

    fun addPushMsgListener(listener: IChatRoomRepository.NewMsgListener) {
        val dealer = msgDealerS.firstOrNull {
            it is PushMsgDealer
        } ?: return
        (dealer as PushMsgDealer).addPushMsgListener(listener)
    }


}