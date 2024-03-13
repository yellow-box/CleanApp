package com.example.domain.socket

import com.example.domain.Api

interface ILogicAction:Api {
    fun initSetting(socket:ISocket,executor: Executor)
    fun connect(host: String, port: Int)
    fun disconnect()
    fun parseServeMsg(byteArray: ByteArray)
    fun startReadAlways()
    fun write(byteArray: ByteArray)

    fun sendHeartBeat()
}