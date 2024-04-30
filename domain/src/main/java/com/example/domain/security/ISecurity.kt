package com.example.domain.security

import com.example.domain.Api

interface ISecurity : Api {
    fun aes_enc(byteArray: ByteArray): ByteArray
    fun aes_dec(byteArray: ByteArray): ByteArray
    fun rsa_dec(byteArray: ByteArray): ByteArray
    fun rsa_enc(byteArray: ByteArray): ByteArray

    //生成RSA秘钥对
    fun rsa_gen(): String
}