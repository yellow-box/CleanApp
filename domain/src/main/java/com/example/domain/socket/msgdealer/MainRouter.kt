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
        println("client receive from server seq:${parseResult.first},:type:${parseResult.second},content:${parseResult.third}")
        msgDealerS.forEach {
            if (it.matchType(parseResult.second)) {
                it.dealData(parseResult.third)
            }
        }
    }


}