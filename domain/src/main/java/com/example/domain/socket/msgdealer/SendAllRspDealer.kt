package com.example.domain.socket.msgdealer

import com.example.domain.socket.DataOperator
import com.example.domain.socket.ISocketMsgDealer

class SendAllRspDealer:ISocketMsgDealer {
    override fun matchType(msgType: Int): Boolean {
        return msgType == DataOperator.OP_SEND_MESSAGE_ALL
    }

    override fun dealData( byteArray: ByteArray) {
        TODO("Not yet implemented")
    }
}