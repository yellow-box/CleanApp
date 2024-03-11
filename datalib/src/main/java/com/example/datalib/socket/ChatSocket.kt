package com.example.datalib.socket

import com.example.domain.socket.ISocket
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class ChatSocket : ISocket {
    private var realSocket: Socket? = null
    private var isConnected = false
    override fun connect(host: String, port: Int) {
        realSocket = Socket()
        try {
            realSocket!!.connect(InetSocketAddress(host, port))
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
        realSocket
    }

    override fun read(byteArray: ByteArray): Int {
        while (true){
            if (realSocket?.isConnected == true){
                return 0
            }
            val curRead = 0

        }
    }

    override fun isConnected(): Boolean {
        return realSocket != null && isConnected
    }
}