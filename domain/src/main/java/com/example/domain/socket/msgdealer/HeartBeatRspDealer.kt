package com.example.domain.socket.msgdealer

import com.example.domain.socket.ISocketMsgDealer
import com.example.domain.socket.OpConst

class HeartBeatRspDealer:ISocketMsgDealer {
    override fun matchType(msgType: Int): Boolean {
        return msgType == OpConst.OP_HEART_BEAT
    }

    override fun dealData(byteArray: ByteArray) {

    }
}