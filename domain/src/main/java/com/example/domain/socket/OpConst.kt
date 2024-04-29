package com.example.domain.socket

object OpConst {
    //绑定用户到socket
    const val OP_BIND_USER = 1

    //心跳包
    const val OP_HEART_BEAT = 2

    // 发送消息
    const val OP_SEND_MESSAGE_ALL = 3

    // push的消息
    const val OP_PUSH_MESSAGE = 5
    const val OP_CREATE_ROOM = 6
    const val OP_JOIN_ROOM = 7

    //交换RSA
    const val OP_EXCHANGE_RSA = 8

    // 传递对称秘钥
    const val OP_SYMMETRIC_ENCRYPTION = 9
}