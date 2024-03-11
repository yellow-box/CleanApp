package com.example.domain.socket

import java.io.InputStream
import java.io.OutputStream
import kotlin.coroutines.coroutineContext

class SocketManager : ILogicAction, ISocket {
    private var socket: ISocket? = null
    private var executor: Executor? = null
    private var reader : InputStream? =null
    private var writer:OutputStream?= null
    override fun initSetting(socket: ISocket, executor: Executor) {
        this.socket = socket
        this.executor = executor
    }

    override fun connect(host: String, port: Int) {
        socket?.connect(host, port)
    }

    override fun disconnect() {
        socket?.disconnect()
    }

    override fun write(byteArray: ByteArray) {
        executor?.runInChild{
            writer?.write(byteArray.size)
            writer?.write(byteArray)
        }
    }

    override fun read(byteArray: ByteArray): Int {
        executor.run {

        }
        return socket?.read(byteArray) ?: 0
    }

    override fun isConnected(): Boolean {
        return socket != null && socket!!.isConnected()
    }

    override fun parseServeMsg(byteArray: ByteArray) {

    }

    override fun sendHeartBeat() {

    }
}