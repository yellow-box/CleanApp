package com.example.domain.socket.msgdealer

import com.example.domain.ApiService
import com.example.domain.security.CommunicateSate
import com.example.domain.socket.ILogicAction
import com.example.domain.socket.ISocketMsgDealer
import com.example.domain.socket.OpConst
import com.example.domain.security.CommunicateStateManager

class SymmetricEncryptionDealer : ISocketMsgDealer {
    override fun matchType(msgType: Int): Boolean {
        return msgType == OpConst.OP_SYMMETRIC_ENCRYPTION
    }

    override fun dealData(byteArray: ByteArray) {
        if (!checkState()) {
            return
        }
        val socketManager = ApiService[ILogicAction::class.java]
        socketManager.rsa_dec(byteArray,"/rsa_pri")
    }

    fun checkState(): Boolean {
        return CommunicateStateManager.state == CommunicateSate.ENCRYPTION_COMMUNICATION
    }
}