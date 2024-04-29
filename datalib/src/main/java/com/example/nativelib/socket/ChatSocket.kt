package com.example.datalib.socket

import com.example.domain.base.printExceptionCallStack
import com.example.domain.base.printlnCallStack
import com.example.domain.socket.ISocket
import com.example.nativelib.NativeLib
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
        realSocket = Socket(host, port)
        println("success connect to ${host}:${port}")
        isConnected = true
        reader = realSocket!!.getInputStream()
        writer = realSocket!!.getOutputStream()
        connectListener?.onConnect()
    }

    override fun aes_enc(byteArray: ByteArray, key: String, iv: String): ByteArray {
        return NativeLib.instance.aes_enc(byteArray, key, iv)
    }

    override fun aes_dec(byteArray: ByteArray, key: String, iv: String): ByteArray {
        return NativeLib.instance.aes_dec(byteArray, key, iv)
    }

    override fun rsa_dec(byteArray: ByteArray, path: String): ByteArray {
        return byteArray
    }

    override fun rsa_enc(byteArray: ByteArray, path: String): ByteArray {
        return byteArray
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
        writer?.flush()
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