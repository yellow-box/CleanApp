package com.example.domain.socket

/**
 * 对socket的抽象
 */
interface ISocket {
    fun connect(host: String, port: Int)
    fun aes_enc(byteArray: ByteArray, key: String, iv: String): ByteArray
    fun aes_dec(byteArray: ByteArray, key: String, iv: String): ByteArray
    fun rsa_dec(byteArray: ByteArray, path: String): ByteArray
    fun rsa_enc(byteArray: ByteArray, path: String): ByteArray
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