package com.example.domain.socket

interface ILogicAction {
    fun initSetting(socket:ISocket,executor: Executor)
    fun connect(host: String, port: Int)
    fun disconnect()
    fun parseServeMsg(byteArray: ByteArray)


    fun sendHeartBeat()
}