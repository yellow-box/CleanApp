package com.example.domain.socket.msgdealer

import com.example.domain.socket.ISocketMsgDealer
import com.example.domain.socket.OpConst
import java.security.Key
import javax.crypto.Cipher

class ExchangeRsaDealer : ISocketMsgDealer {
    override fun matchType(msgType: Int): Boolean {
        return msgType == OpConst.OP_EXCHANGE_RSA
    }

    override fun dealData(byteArray: ByteArray) {
        TODO("Not yet implemented")
    }

    fun encryptRSA(data: ByteArray, key: Key): ByteArray {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher.doFinal(data)
    }
}