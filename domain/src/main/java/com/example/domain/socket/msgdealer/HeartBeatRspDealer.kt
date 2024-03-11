package com.example.domain.socket.msgdealer

import com.example.domain.socket.DataOperator
import com.example.domain.socket.ISocketMsgDealer

class HeartBeatRspDealer:ISocketMsgDealer {
    override fun matchType(msgType: Int): Boolean {
        return msgType == DataOperator.OP_HEART_BEAT
    }

    override fun dealData(byteArray: ByteArray) {

    }
}