package com.example.domain.security

object CommunicateStateManager {
    var state: CommunicateSate = CommunicateSate.ESTABLISH
}

enum class CommunicateSate {
    //刚建立
    ESTABLISH,

    //交换RSA
    EXCHANGE_RSA,

    //使用对称秘钥
    SYMMETRIC_ENCRYPTION,

    //交换秘钥完成
    ENCRYPTION_COMMUNICATION
}