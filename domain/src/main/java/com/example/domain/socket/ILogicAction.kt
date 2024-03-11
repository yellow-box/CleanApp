package com.example.domain.socket

interface ILogicAction {
    fun initSetting(socket:ISocket,executor: Executor)
    fun parseServeMsg(byteArray: ByteArray)


    fun sendHeartBeat()
}