package com.example.domain.socket


import com.example.domain.ApiService
import com.example.domain.device.ILoginUser
import com.example.domain.logic.user.User
import com.example.domain.socket.msgdealer.MainRouter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class SocketManager : ILogicAction {
    private var socket: ISocket? = null
    private var executor: Executor? = null
    private val bufSize = 512
    private val buf = ByteArray(bufSize)
    private val buf2 = ByteArray(4)
    private val mainRouter by lazy { MainRouter() }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SocketManager() }
    }

    override fun initSetting(socket: ISocket, executor: Executor) {
        this.socket = socket
        this.executor = executor
    }

    override fun connect(host: String, port: Int) {
        socket?.connect(host, port)
        //约定 连接后 发送的前4个字节为uid
        bindUser(ApiService[ILoginUser::class.java]?.getUid()?:0)
    }

    fun bindUser(uid:Int){
        socket?.write(uid.toByteArray())
    }

    override fun disconnect() {
        socket?.disconnect()
    }

    override fun write(byteArray: ByteArray) {
        executor?.runInChild {
            //先写要读的数据的长度
            socket?.write(byteArray.size.toByteArray())
            socket?.write(byteArray)
        }
    }


    override fun startReadAlways() {
        executor?.runInChild {
            while (socket?.isConnected() == true) {
                val sc = socket ?: return@runInChild
                //先读后续数据的长度
                sc.read(buf2)
                val target = ByteBuffer.wrap(buf2).order(ByteOrder.LITTLE_ENDIAN).getInt()
                var curRead = 0
                val byteBuffer = ByteBuffer.allocate(target)
                while (curRead < target) {
                    curRead += sc.read(buf)
                    if (curRead < buf.size) {
                        for (i in 0 until curRead) {
                            byteBuffer.put(buf[i])
                        }
                    } else {
                        byteBuffer.put(buf)
                    }
                }
                parseServeMsg(byteBuffer.array())
            }
        }
    }

    fun isConnected(): Boolean {
        return socket != null && socket!!.isConnected()
    }

    override fun parseServeMsg(byteArray: ByteArray) {
        mainRouter.dealData(byteArray)
    }

    override fun sendHeartBeat() {
        val data = DataOperator.constructData(DataOperator.OP_HEART_BEAT, ByteArray(0))
        write(data)
    }

    fun Int.toByteArray(): ByteArray {
        val buffer = ByteBuffer.allocate(4)
        buffer.putInt(this)
        return buffer.array()
    }

}