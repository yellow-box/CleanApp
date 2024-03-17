package com.example.domain.socket

import com.example.domain.Api

interface ILogicAction : Api {
    fun initSetting(socket: ISocket, executor: Executor)
    fun connect(host: String, port: Int)
    fun disconnect()
    fun isConnected(): Boolean
    fun parseServeMsg(byteArray: ByteArray)
    fun startReadAlways()
    fun write(byteArray: ByteArray, seq: Long, seqCallback: SeqCallback? = null)

    fun sendHeartBeat()

    interface SeqCallback {
        fun onSuccess()

        fun onFail(code: Int, msg: String)
    }
}