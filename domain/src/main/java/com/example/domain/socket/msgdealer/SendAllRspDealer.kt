package com.example.domain.socket.msgdealer

import com.example.domain.socket.ISocketMsgDealer
import com.example.domain.socket.OpConst

class SendAllRspDealer:ISocketMsgDealer {
    override fun matchType(msgType: Int): Boolean {
        return msgType == OpConst.OP_SEND_MESSAGE_ALL
    }

    override fun dealData( byteArray: ByteArray) {

    }
}