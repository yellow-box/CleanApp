package com.example.domain.socket


import com.example.domain.ApiService
import com.example.domain.base.GsonUtil
import com.example.domain.base.printExceptionCallStack
import com.example.domain.device.ILoginUser
import com.example.domain.memostore.InMemoDataCallback
import com.example.domain.socket.msgdealer.BindUserData
import com.example.domain.socket.msgdealer.MainRouter
import com.example.domain.socket.msgdealer.RawDataStruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class SocketManager : ILogicAction {
    companion object {
        val HEART_BEAT_INTERNAL = 1000L
    }

    private var socket: ISocket? = null
    private var executor: Executor? = null
    private val mainRouter by lazy { MainRouter.instance }
    private val dataSizeBuf = ByteArray(4)

    override fun initSetting(socket: ISocket, executor: Executor) {
        this.socket = socket
        this.executor = executor
        socket.setOnConnectListener(object : ISocket.ConnectListener {
            override fun onConnect() {
                sendHeartBeat()
                startReadAlways()
                CoroutineScope(Dispatchers.IO).launch {
                    ApiService[ILoginUser::class.java].getUid().collect {
                        bindUser(it)
                    }
                }
            }
        })
    }

    override fun connect(host: String, port: Int) {
        if (isConnected()) {
            return
        }
        executor?.runInChild {
            try {
                socket?.connect(host, port)
            } catch (e: Exception) {
                printExceptionCallStack("fail to connect ", e)
            }
        }
    }

    fun bindUser(uid: Int) {
        val dataOperator = ApiService[RawDataOperator::class.java] ?: return
        val outData = dataOperator.constructData(
            RawDataStruct(
                0L, OpConst.OP_BIND_USER,
                GsonUtil.gson.toJson(BindUserData(uid)).toByteArray(), "bindUser"
            )
        )
        write(outData.first, outData.second, null)
    }

    override fun disconnect() {
        socket?.disconnect()
        HeatBeatManager.instance.cancelTimer()
    }

    // 其他的write和 heatBeat 的 write 会导致 先写数据长度，后写数据内容的 写入顺序被破坏。
    override fun write(byteArray: ByteArray, seq: Long, seqCallback: ILogicAction.SeqCallback?) {
        executor?.runInChildWithMutex {
            try {
                seqCallback?.apply {
                    if (seq >= 0) {
                        MainRouter.instance.putCallback(seq, this)
                    }
                }
                //先写要读的数据的长度
                socket?.write(byteArray.size.toByteArray())
                socket?.write(byteArray)
            } catch (e: SocketException) {
                disconnect()
            }
        }
    }


    override fun startReadAlways() {
        executor?.runInChild {
            while (socket?.isConnected() == true) {
                val sc = socket ?: return@runInChild
                try {
                    //先读后续数据的长度
                    sc.read(dataSizeBuf)
                    val target =
                        ByteBuffer.wrap(dataSizeBuf).order(ByteOrder.LITTLE_ENDIAN).getInt()
                    val dataBuf = ByteArray(target)
                    sc.read(dataBuf)
                    val byteBuffer = ByteBuffer.wrap(dataBuf).order(ByteOrder.LITTLE_ENDIAN)
                    //
                    parseServeMsg(byteBuffer.array())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun isConnected(): Boolean {
//        println("soekt==null =${socket==null},socket.IsConected=${socket?.isConnected()}")
        return socket != null && socket!!.isConnected()
    }

    override fun parseServeMsg(byteArray: ByteArray) {
        mainRouter.dealData(byteArray)
    }

    override fun sendHeartBeat() {
        HeatBeatManager.instance.start()
    }

    fun Int.toByteArray(): ByteArray {
        val buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
        buffer.putInt(this)
        return buffer.array()
    }

}