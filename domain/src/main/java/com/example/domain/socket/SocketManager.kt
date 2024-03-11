package com.example.domain.socket


import java.nio.ByteBuffer

class SocketManager : ILogicAction {
    private var socket: ISocket? = null
    private var executor: Executor? = null
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

    fun write(byteArray: ByteArray) {
        executor?.runInChild {
            socket?.write(byteArray.size.toByteArray())
            socket?.write(byteArray)
        }
    }

    fun read() {
        executor?.runInChild{
            while (true){
                socket?.read()
            }
        }

    }

    fun isConnected(): Boolean {
        return socket != null && socket!!.isConnected()
    }

    override fun parseServeMsg(byteArray: ByteArray) {

    }

    override fun sendHeartBeat() {

    }

    fun Int.toByteArray(): ByteArray {
        val buffer = ByteBuffer.allocate(4)
        buffer.putInt(this)
        return buffer.array()
    }
}