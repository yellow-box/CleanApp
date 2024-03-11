package com.example.domain.socket

interface ISocketMsgDealer {

    //是否处理该消息
    fun matchType(msgType: Int): Boolean

    //具体处理消息的逻辑
    fun dealData(byteArray: ByteArray)
}