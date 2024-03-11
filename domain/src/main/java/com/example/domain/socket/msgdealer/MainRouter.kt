package com.example.domain.socket.msgdealer

import com.example.domain.socket.DataOperator
import com.example.domain.socket.ISocketMsgDealer
import java.lang.Exception
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MainRouter {

    private val msgDealerS: MutableList<ISocketMsgDealer> = ArrayList()

    init {
        msgDealerS.add(BindUserDealer())
    }

    fun dealData(byteArray: ByteArray) {
        val parseResult = DataOperator.parseRawData(byteArray)
        msgDealerS.forEach {
            if (it.matchType(parseResult.second)) {
                it.dealData(parseResult.third)
            }
        }
    }


}