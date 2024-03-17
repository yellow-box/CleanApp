package com.example.domain.logic.chat

import com.example.domain.ApiService
import com.example.domain.socket.ILogicAction


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
        val socketManager = ApiService[ILogicAction::class.java] ?: return
//        val rawDataOperator = ApiService[RawDataOperator<RawDataStruct>::class.java] ?: return
//
//        socketManager.write(
//            rawDataOperator.constructData(
//                RawDataStruct(
//                    0L,
//                    OpConst.OP_SEND_MESSAGE_ALL,
//                    GsonUtil.gson.toJson(roomMsg).toByteArray()
//                )
//            )
//        )
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
