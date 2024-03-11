package com.example.domain.socket

interface ISocket {
    fun connect(host: String, port: Int)

    fun disconnect()

    fun write(byteArray: ByteArray)

    fun read(byteArray: ByteArray): Int
    fun isConnected():Boolean

}