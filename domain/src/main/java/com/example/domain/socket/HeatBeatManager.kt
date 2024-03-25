package com.example.domain.socket

import com.example.domain.ApiService
import com.example.domain.socket.msgdealer.RawDataStruct
import java.util.Timer
import java.util.TimerTask

class HeatBeatManager {
    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { HeatBeatManager() }
    }

    private var checkRspTimer: Timer? = null
    private var sendTimer: Timer? = null
    private var lastRecHeatBeatResp = 0L

    fun startSendHeartBeat() {
        sendTimer = Timer()
        sendTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                ApiService[ILogicAction::class.java]?.apply {
                    if (!isConnected()) {
                        println("heartbeat not connected")
                        cancelTimer()
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
                    println("send heart beat seq=${data.second}")
                    write(data.first, data.second)
                }
            }
        }, 0L, SocketManager.HEART_BEAT_INTERNAL)
    }

    fun startCheck() {
        lastRecHeatBeatResp = System.currentTimeMillis()
        checkRspTimer = Timer()
        checkRspTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val cur = System.currentTimeMillis()
                if ((cur - lastRecHeatBeatResp) > (SocketManager.HEART_BEAT_INTERNAL + 1000)) {
                    ApiService[ILogicAction::class.java]?.disconnect()
                    cancelTimer()
                } else {
                    lastRecHeatBeatResp = cur
                }
            }
        }, 0, SocketManager.HEART_BEAT_INTERNAL)
    }

    fun start() {
        startSendHeartBeat()
        startCheck()
    }

    fun cancelTimer() {
        checkRspTimer?.cancel()
        checkRspTimer = null

        sendTimer?.cancel()
        sendTimer = null
    }


}