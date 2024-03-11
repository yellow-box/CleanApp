package com.example.domain.socket.msgdealer

import com.example.domain.socket.DataOperator
import com.example.domain.socket.ISocketMsgDealer

class BindUserDealer : ISocketMsgDealer {
    override fun matchType(msgType: Int): Boolean {
        return msgType == DataOperator.OP_BIND_USER
    }

    override fun dealData(byteArray: ByteArray) {

    }
}