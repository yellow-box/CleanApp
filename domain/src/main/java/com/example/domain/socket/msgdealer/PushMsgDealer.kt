package com.example.domain.socket.msgdealer

import com.example.domain.base.GsonUtil
import com.example.domain.logic.chat.IChatRoomRepository
import com.example.domain.logic.chat.RoomMsg
import com.example.domain.socket.DataOperator
import com.example.domain.socket.ISocketMsgDealer

class PushMsgDealer:ISocketMsgDealer {
    private val pushMsgListeners: MutableList<IChatRoomRepository.NewMsgListener> = mutableListOf()
    override fun matchType(msgType: Int): Boolean {
        return msgType == DataOperator.OP_PUSH_MESSAGE
    }

    override fun dealData( byteArray: ByteArray) {
        pushMsgListeners.forEach {
            it.onReceiveNewMsg(GsonUtil.gson.fromJson(String(byteArray) , RoomMsg::class.java))
        }
    }

    fun addPushMsgListener(listener: IChatRoomRepository.NewMsgListener) {
        pushMsgListeners.add(listener)
    }

    fun removePushMsgListener(listener: IChatRoomRepository.NewMsgListener) {
        pushMsgListeners.remove(listener)
    }
}