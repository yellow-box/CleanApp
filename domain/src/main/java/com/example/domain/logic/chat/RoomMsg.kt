package com.example.domain.logic.chat

import com.google.gson.annotations.SerializedName

/**
 * 网络传输使用的 房间消息的数据模型
 */
class RoomMsg {
    @SerializedName("room_id")
    var roomId = 0

    @SerializedName("content")
    var content: String = ""

    @SerializedName("from_uid")
    var sendUid: Int = 0

    @SerializedName("msg_id")
    var msgId: String = ""

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is RoomMsg) return false
        return roomId == other.roomId && content == other.content && sendUid == other.sendUid && msgId == other.msgId
    }

    override fun hashCode(): Int {
        var result = roomId
        result = 31 * result + content.hashCode()
        result = 31 * result + sendUid
        result = 31 * result + msgId.hashCode()
        return result
    }
}