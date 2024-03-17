package com.example.datalib.socket

import com.example.domain.base.printExceptionCallStack
import com.example.domain.base.printlnCallStack
import com.example.domain.socket.ISocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private var connectListener: ISocket.ConnectListener? = null
    override fun connect(host: String, port: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                realSocket = Socket(host, port)
                println("success connect to ${host}:${port}")
                isConnected = true
                reader = realSocket!!.getInputStream()
                writer = realSocket!!.getOutputStream()
                connectListener?.onConnect()
            } catch (e: Exception) {
                printExceptionCallStack("fail to connect ", e)
            }
        }
    }


    override fun disconnect() {
        println("disconnect")
        reader?.close()
        writer?.close()
        realSocket?.close()
        realSocket = null
    }

    override fun setOnConnectListener(l: ISocket.ConnectListener) {
        this.connectListener = l
    }

    override fun write(byteArray: ByteArray) {
        writer?.write(byteArray)
    }

    override fun read(byteArray: ByteArray): Int {
        return read(byteArray, 0, byteArray.size)
    }

    override fun read(byteArray: ByteArray, startIndex: Int, length: Int): Int {
        return reader?.read(byteArray, startIndex, length) ?: -1
    }

    override fun isConnected(): Boolean {
        return realSocket != null && isConnected
    }
}