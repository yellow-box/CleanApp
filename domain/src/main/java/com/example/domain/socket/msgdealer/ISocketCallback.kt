package com.example.domain.socket.msgdealer

interface ISocketCallback {
    fun onSuccess(data: ByteArray)

    fun onFail(msg: String)
}