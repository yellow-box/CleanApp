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
    private val rsaPubName= "rsa_pub"
    private val rsaPriName ="rsa_pri"
    private val targetRsaPubPath = "${context.dataDir.path}/$rsaPubName"
    private val targetRsaPriPath = "${context.dataDir.path}/$rsaPriName"
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
        return NativeLib.instance.rsa_dec(byteArray, targetRsaPubPath)
    }

    override fun rsa_enc(byteArray: ByteArray): ByteArray {
        return NativeLib.instance.rsa_enc(byteArray, targetRsaPriPath)
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
        return (File(targetRsaPriPath).exists() && File(targetRsaPubPath).exists())
    }

    private fun saveKeyPairToFile() {
        ApiService[Executor::class.java].runInChild {
            context.assets.open("rsa_pub")
                .copyTo(FileOutputStream(File(targetRsaPubPath)))
            context.assets.open("rsa_pri")
                .copyTo(FileOutputStream(File(targetRsaPriPath)))
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