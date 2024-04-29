package com.example.domain.security

interface ISecurity {
    fun aes_enc(byteArray: ByteArray, key: String, iv: String): ByteArray
    fun aes_dec(byteArray: ByteArray, key: String, iv: String): ByteArray
    fun rsa_dec(byteArray: ByteArray, path: String): ByteArray
    fun rsa_enc(byteArray: ByteArray, path: String): ByteArray
    //生成RSA秘钥对
    fun rsa_gen():String
}