package com.example.nativelib.security

import android.app.Application
import android.util.Log
import com.example.domain.ApiService
import com.example.domain.device.IGlobalContextProvider
import com.example.domain.security.ISecurity
import com.example.domain.socket.Executor
import com.example.nativelib.NativeLib
import java.io.File
import java.io.FileOutputStream
import java.math.BigInteger
import javax.crypto.KeyGenerator


class SecurityImpl : ISecurity {
    private val context = ApiService[IGlobalContextProvider::class.java].getContext() as Application
    private val aesKey: String = ""
    private val aesIv: String = ""

    init {
        rsa_gen()
    }

    override fun aes_enc(byteArray: ByteArray): ByteArray {
        return NativeLib.instance.aes_enc(byteArray, "dvrugkq9amu7wj0s", "dvrugkq9amu7wj0s")
    }

    override fun aes_dec(byteArray: ByteArray): ByteArray {
        return NativeLib.instance.aes_dec(byteArray, "dvrugkq9amu7wj0s", "dvrugkq9amu7wj0s")
    }

    override fun rsa_dec(byteArray: ByteArray): ByteArray {
        return NativeLib.instance.rsa_dec(byteArray, context.dataDir.path + "rsa_pub")
    }

    override fun rsa_enc(byteArray: ByteArray): ByteArray {
        return NativeLib.instance.rsa_enc(byteArray, context.dataDir.path + "rsa_pri")
    }

    override fun rsa_gen(): String {
        if (!isKeyPairFileExist()) {
            saveKeyPairToFile()
        } else {
            Log.d("[security]", " rsa key pair file exists")
        }
        return ""
    }

    private fun isKeyPairFileExist(): Boolean {
        return (File(context.dataDir.path + "rsa_pub").exists() && File(context.dataDir.path + "rsa_pri").exists())
    }

    private fun saveKeyPairToFile() {
        ApiService[Executor::class.java].runInChild {
            context.assets.open("rsa_pub")
                .copyTo(FileOutputStream(File(context.dataDir.path + "rsa_pub")))
            context.assets.open("rsa_pri")
                .copyTo(FileOutputStream(File(context.dataDir.path + "rsa_pri")))
            Log.d("[security]", "save rsa key pair  to file ")
        }
    }

    private fun generateAesKey() {
        val gen = KeyGenerator.getInstance("AES")
        gen.init(128) /* 128-bit AES */
        val secret = gen.generateKey()
        val binary = secret.encoded
        val text = String.format("%032X", BigInteger(+1, binary))
    }
}