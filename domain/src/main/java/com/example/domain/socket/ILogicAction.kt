package com.example.domain.socket

import com.example.domain.Api
import com.example.domain.security.ISecurity

/**
 * 对socket的业务逻辑的抽象
 */
interface ILogicAction : Api {
    fun initSetting(socket: ISocket, executor: Executor, security: ISecurity)
    fun connect(host: String, port: Int)
    fun disconnect()
    fun isConnected(): Boolean
    fun parseServeMsg(byteArray: ByteArray)
    fun startReadAlways()
    fun write(byteArray: ByteArray, seq: Long, seqCallback: SeqCallback? = null)

    fun rsa_enc(byteArray: ByteArray): ByteArray
    fun rsa_dec(byteArray: ByteArray): ByteArray

    fun aes_enc(byteArray: ByteArray): ByteArray

    fun aes_dec(byteArray: ByteArray): ByteArray

    fun sendHeartBeat()
    interface SeqCallback {
        fun onSuccess()

        fun onFail(code: Int, msg: String)
    }
}