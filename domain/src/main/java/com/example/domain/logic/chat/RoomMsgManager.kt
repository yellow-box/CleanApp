package com.example.domain.logic.chat

import com.example.domain.socket.msgdealer.MainRouter
import com.example.domain.socket.msgdealer.PushMsgDealer
import java.util.LinkedList

class RoomMsgManager : IChatRoomRepository.NewMsgListener {
    private val msgS: MutableMap<Int, MutableList<RoomMsg>> = HashMap()
    var pushMsgListener: PushMsgListener? = null

    init {
        MainRouter.instance.addPushMsgListener(this)
    }

    companion object {
        private val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { RoomMsgManager() }
        fun getManger(): RoomMsgManager {
            return instance
        }
    }

    fun onRecRoomMsg(roomId: Int, content: String, fromUid: Int) {
        if (!msgS.containsKey(roomId)) {
            msgS[roomId] = LinkedList()
        } else {
            msgS[roomId]?.add(RoomMsg().apply {
                this.roomId = roomId
                this.content = content
                this.sendUid = fromUid
            })
        }
    }

    private fun addRoomMsg(roomMsg: RoomMsg) {
        val roomId = roomMsg.roomId
        if (!msgS.containsKey(roomId)) {
            msgS[roomId] = LinkedList()
        } else {
            msgS[roomId]?.add(roomMsg)
        }
    }

    fun getMsgSByRoomId(roomId: Int): List<RoomMsg>? {
        return msgS[roomId]
    }

    override fun onReceiveNewMsg(roomMsg: RoomMsg) {
        addRoomMsg(roomMsg)
        pushMsgListener?.onPushMsg(roomMsg)
    }
}

interface PushMsgListener {
    fun onPushMsg(roomMsg: RoomMsg)
}