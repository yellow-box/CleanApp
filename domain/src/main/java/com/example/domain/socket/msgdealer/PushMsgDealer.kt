package com.example.domain.socket.msgdealer

import com.example.domain.base.GsonUtil
import com.example.domain.logic.chat.RoomMsg
import com.example.domain.socket.ISocketMsgDealer
import com.example.domain.socket.OpConst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * push消息的处理
 */
class PushMsgDealer : ISocketMsgDealer {

    private val _roomPushMsgFlow = MutableSharedFlow<RoomMsg>()
    val roomPushMsgFlow = _roomPushMsgFlow.asSharedFlow()

    override fun matchType(msgType: Int): Boolean {
        return msgType == OpConst.OP_PUSH_MESSAGE
    }

    override fun dealData(byteArray: ByteArray) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _roomPushMsgFlow.emit(
                    GsonUtil.gson.fromJson(
                        String(byteArray),
                        RoomMsg::class.java
                    )
                )
            } catch (e: Exception) {
                println("push Msg Deal Data error:${e}")
            }
        }


    }
}