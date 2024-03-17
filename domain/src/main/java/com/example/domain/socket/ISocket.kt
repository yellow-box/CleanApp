package com.example.domain.socket

interface ISocket {
    fun connect(host: String, port: Int)

    fun disconnect()

    fun setOnConnectListener(l: ConnectListener)
    fun write(byteArray: ByteArray)

    fun read(byteArray: ByteArray): Int

    fun read(byteArray: ByteArray, startIndex: Int, length: Int): Int
    fun isConnected(): Boolean
    interface ConnectListener {
        fun onConnect()
    }

}