package com.example.domain.socket.msgdealer

import com.example.domain.socket.DataOperator
import com.example.domain.socket.ISocketMsgDealer

class PushMsgDealer:ISocketMsgDealer {
    override fun matchType(msgType: Int): Boolean {
        return msgType == DataOperator.OP_PUSH_MESSAGE
    }

    override fun dealData( byteArray: ByteArray) {
        TODO("Not yet implemented")
    }
}