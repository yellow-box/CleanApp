package com.example.domain.logic.chat

object ChatModelMapping {
    fun toViewModel(roomMsg: RoomMsg): VRoomMsg {
        return VRoomMsg().apply {
            roomId = roomMsg.roomId
            sender = roomMsg.sendUid
            content = roomMsg.content
            msgId = roomMsg.msgId
        }
    }

    fun toDataModel(msg: VRoomMsg): RoomMsg {
        return RoomMsg().apply {
            roomId = msg.roomId
            sendUid = msg.sender
            content = msg.content
        }
    }

    fun toViewModels(msgS: List<RoomMsg>): List<VRoomMsg> {
        return msgS.map {
            toViewModel(it)
        }
    }
}