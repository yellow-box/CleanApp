package com.example.domain.socket


import com.example.domain.ApiService
import com.example.domain.device.ILoginUser
import com.example.domain.socket.msgdealer.MainRouter
import com.example.domain.socket.msgdealer.RawDataStruct
import java.net.SocketException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Timer
import java.util.TimerTask

class SocketManager : ILogicAction {
    private var socket: ISocket? = null
    private var executor: Executor? = null

    private val mainRouter by lazy { MainRouter.instance }
    private var timer: Timer? = null
    override fun initSetting(socket: ISocket, executor: Executor) {
        this.socket = socket
        this.executor = executor
        socket.setOnConnectListener(object : ISocket.ConnectListener {
            override fun onConnect() {
//                sendHeartBeat()
                startReadAlways()
                bindUser(ApiService[ILoginUser::class.java]?.getUid() ?: 0)
            }
        })
    }

    override fun connect(host: String, port: Int) {
        if (isConnected()) {
            return
        }
        socket?.connect(host, port)
    }

    fun bindUser(uid: Int) {
        write(uid.toByteArray(), -1)
    }

    override fun disconnect() {
        socket?.disconnect()
        timer?.cancel()
    }

    override fun write(byteArray: ByteArray, seq: Long, callback: ILogicAction.SeqCallback?) {
        executor?.runInChild {
            try {
                callback?.apply {
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
            while (true) {
                if (socket?.isConnected() == true) {
                    val bufSize = 512
                    val buf = ByteArray(bufSize)
                    val buf2 = ByteArray(4)
                    val sc = socket ?: return@runInChild
                    try {
                        //先读后续数据的长度
                        sc.read(buf2)
                        val target = ByteBuffer.wrap(buf2).order(ByteOrder.LITTLE_ENDIAN).getInt()
                        var curRead = 0
                        val byteBuffer = ByteBuffer.allocate(target).order(ByteOrder.LITTLE_ENDIAN)
                        while (curRead < target) {
                            val curPendingReadSize = if ((target - curRead) < buf.size) {
                                target - curRead
                            } else {
                                buf.size
                            }
                            curRead += sc.read(buf, 0, curPendingReadSize)
                            if (curRead < buf.size) {
                                for (i in 0 until curRead) {
                                    byteBuffer.put(buf[i])
                                }
                            } else {
                                byteBuffer.put(buf)
                            }
                        }
                        parseServeMsg(byteBuffer.array())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


            }
        }
    }

    override fun isConnected(): Boolean {
        return socket != null && socket!!.isConnected()
    }

    override fun parseServeMsg(byteArray: ByteArray) {
        mainRouter.dealData(byteArray)
    }

    override fun sendHeartBeat() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (!isConnected()) {
                    println("heartbeat not connected")
                    return
                }
                val dataOperator = ApiService[RawDataOperator::class.java] ?: return
                val rawData = RawDataStruct(
                    0L,
                    OpConst.OP_HEART_BEAT,
                    ByteArray(0),
                    "heartBeat"
                )
                val data =
                    dataOperator.constructData(
                        rawData
                    )
                write(data.first, data.second)
            }
        }, 0L, 2000L)
    }

    fun Int.toByteArray(): ByteArray {
        val buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
        buffer.putInt(this)
        return buffer.array()
    }

}