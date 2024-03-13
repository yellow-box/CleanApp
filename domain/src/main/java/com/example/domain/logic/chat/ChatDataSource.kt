package com.example.domain.logic.chat

import com.example.domain.base.GsonUtil
import com.example.domain.socket.DataOperator
import com.example.domain.socket.SocketManager

class ChatDataSource : IChatRoomRepository.NewMsgListener {
    var receiveMsgListener: IChatRoomRepository.NewMsgListener? = null

    init {
        RoomMsgManager.getManger().pushMsgListener = object : PushMsgListener {
            override fun onPushMsg(roomMsg: RoomMsg) {
                onReceiveNewMsg(roomMsg)
            }
        }
    }

    fun sendMessage(roomId: Int, msg: String, fromUid: Int) {
        val roomMsg = RoomMsg().apply {
            this.roomId = roomId
            this.content = msg
            this.sendUid = fromUid
        }
        SocketManager.instance.write(
            DataOperator.constructData(
                DataOperator.OP_SEND_MESSAGE_ALL,
                GsonUtil.gson.toJson(roomMsg).toByteArray()
            )
        )
    }

    fun fetchHistoryMessages(roomId: Int): List<RoomMsg> {
        return RoomMsgManager.getManger().getMsgSByRoomId(roomId) ?: emptyList()
    }


    fun clear() {
        receiveMsgListener = null
        RoomMsgManager.getManger().pushMsgListener = null
    }

    override fun onReceiveNewMsg(roomMsg: RoomMsg) {

    }
}
