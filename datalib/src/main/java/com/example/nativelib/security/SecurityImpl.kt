package com.example.nativelib.security

import com.example.domain.security.ISecurity
import com.example.nativelib.NativeLib

class SecurityImpl : ISecurity {
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

    override fun rsa_gen(): String {
        TODO("Not yet implemented")
    }
}