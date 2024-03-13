package com.example.domain.logic.chat

import com.example.domain.base.GsonUtil
import com.example.domain.socket.DataOperator
import com.example.domain.socket.SocketManager


class ChatRoomRepository : IChatRoomRepository{

    override fun registerNewMsgListener(l: IChatRoomRepository.NewMsgListener) {
        RoomMsgManager.getManger().pushMsgListener = object : PushMsgListener {
            override fun onPushMsg(roomMsg: RoomMsg) {
                l.onReceiveNewMsg(roomMsg)
            }
        }
    }

    override fun clear() {
        RoomMsgManager.getManger().pushMsgListener = null
    }

    override fun sendMsg(content: String, roomId: Int, uid: Int) {
        val roomMsg = RoomMsg().apply {
            this.roomId = roomId
            this.content = content
            this.sendUid = uid
        }
        SocketManager.instance.write(
            DataOperator.constructData(
                DataOperator.OP_SEND_MESSAGE_ALL,
                GsonUtil.gson.toJson(roomMsg).toByteArray()
            )
        )
    }

    override fun loadOldMsg(roomId: Int): List<RoomMsg> {
        return RoomMsgManager.getManger().getMsgSByRoomId(roomId) ?: emptyList()
    }

}