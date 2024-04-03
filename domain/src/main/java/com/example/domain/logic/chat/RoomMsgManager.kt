package com.example.domain.logic.chat

import com.example.domain.ApiService
import com.example.domain.common.Callback
import com.example.domain.db.DbCallback
import com.example.domain.db.IChatMsgFetcher
import com.example.domain.socket.msgdealer.MainRouter
import java.util.LinkedList
import java.util.concurrent.atomic.AtomicBoolean

class RoomMsgManager : IChatRoomRepository.NewMsgListener {
    private val msgS: MutableMap<Int, MutableList<RoomMsg>> = HashMap()
    var pushMsgListener: PushMsgListener? = null
    private var hasInit = AtomicBoolean(false)
    private var loadCallbacks: MutableMap<Int, Callback<List<RoomMsg>>> = HashMap()

    init {
        MainRouter.instance.addPushMsgListener(this)
        ApiService[IChatMsgFetcher::class.java].loadAllRoomMsg(object : DbCallback<List<RoomMsg>> {
            override fun onSuccess(data: List<RoomMsg>) {
                loadAllRoomMsgFromDb(data)
                invokeLoadCallbacks()
                hasInit.set(true)
            }

            override fun onFail(nsg: String) {
                invokeLoadCallbacks()
                println("RoomMsgManager load dbMsgS failed! ${nsg}")
                hasInit.set(true)
            }

        })
    }

    companion object {
        private val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { RoomMsgManager() }
        fun getManger(): RoomMsgManager {
            return instance
        }
    }

    private fun invokeLoadCallbacks() {
        val iterator = loadCallbacks.iterator()
        iterator.forEach {
            it.value.call(msgS[it.key] ?: emptyList())
            iterator.remove()
        }
    }

    private fun addRoomMsg(roomMsg: RoomMsg) {
        val roomId = roomMsg.roomId
        if (!msgS.containsKey(roomId)) {
            msgS[roomId] = LinkedList()
            msgS[roomId]!!.add(roomMsg)
        } else {
            msgS[roomId]?.add(roomMsg)
        }
    }

    private fun loadAllRoomMsgFromDb(msgS: List<RoomMsg>) {
        msgS.forEach {
            addRoomMsg(it)
        }
    }

    fun getMsgSByRoomId(roomId: Int, onCallback: Callback<List<RoomMsg>>) {
        if (hasInit.get()) {
            onCallback.call(msgS[roomId] ?: emptyList())
        } else {
            loadCallbacks[roomId] = onCallback
        }
    }

    override fun onReceiveNewMsg(roomMsg: RoomMsg) {
        addRoomMsg(roomMsg)
        ApiService[IChatMsgFetcher::class.java].saveMsg(roomMsg, null)
        pushMsgListener?.onPushMsg(roomMsg)
    }
}

interface PushMsgListener {
    fun onPushMsg(roomMsg: RoomMsg)
}