package com.example.domain.socket

object OpConst {
    const val OP_BIND_USER = 1

    //心跳包
    const val OP_HEART_BEAT = 2
    const val OP_SEND_MESSAGE_ALL = 3
    const val OP_PUSH_MESSAGE = 5
    const val OP_CREATE_ROOM = 6
    const val OP_JOIN_ROOM = 7
}