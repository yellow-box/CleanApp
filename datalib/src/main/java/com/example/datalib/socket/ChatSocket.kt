package com.example.datalib.socket

import com.example.domain.socket.ISocket
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

class ChatSocket : ISocket {
    private var realSocket: Socket? = null
    private var isConnected = false
    private var reader: InputStream? = null
    private var writer: OutputStream? = null
    override fun connect(host: String, port: Int) {
        realSocket = Socket()
        try {
            realSocket!!.connect(InetSocketAddress(host, port))
            println("success connect to ${host}:${port}")
            reader = realSocket!!.getInputStream()
            writer = realSocket!!.getOutputStream()
        } catch (e: IOException) {
            println("fail to connect to ${host}:${port},e=${e.stackTrace}")
        }
    }

    override fun disconnect() {
        println("disconnect")
        realSocket?.close()
        realSocket = null
    }

    override fun write(byteArray: ByteArray) {
        writer?.write(byteArray)
    }

    override fun read(byteArray: ByteArray): Int {
        return reader?.read(byteArray) ?: -1
    }

    override fun isConnected(): Boolean {
        return realSocket != null && isConnected
    }
}