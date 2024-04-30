package com.example.nativelib

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.datalib.socket.ChatSocket
import com.example.nativelib.security.SecurityImpl
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.nativelib.test", appContext.packageName)
    }

    @Test
    fun get_security_version(){
        val v = NativeLib.instance.stringFromJNI()
        Log.d("NativeLib", v)
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun test_aes_enc() {
        val socket = SecurityImpl()
        val data = "Something to be encrypted".toByteArray()
        val r = socket.aes_enc(data)
        assert(r.contentEquals(Base64.decode("0e3pUSbOuSC/QITsJmLG22rTuGonjXdVTkMIRpAhlxo=")))
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun test_aes_dec() {
        val socket = ChatSocket()
        val data = Base64.decode("0e3pUSbOuSC/QITsJmLG22rTuGonjXdVTkMIRpAhlxo=")
        var r = socket.aes_dec(data, "dvrugkq9amu7wj0s", "dvrugkq9amu7wj0s")
        //PKCS7
        r = r.sliceArray(0 until data.size - r[data.size-1])
        assert(r.decodeToString() == "Something to be encrypted")
    }
}