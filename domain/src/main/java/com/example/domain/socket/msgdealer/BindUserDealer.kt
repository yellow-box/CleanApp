package com.example.domain.socket.msgdealer

import com.example.domain.socket.ISocketMsgDealer
import com.example.domain.socket.OpConst

class BindUserDealer : ISocketMsgDealer {
    override fun matchType(msgType: Int): Boolean {
        return msgType == OpConst.OP_BIND_USER
    }

    override fun dealData(byteArray: ByteArray) {

    }
}