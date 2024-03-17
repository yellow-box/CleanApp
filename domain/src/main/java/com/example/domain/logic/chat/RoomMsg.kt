package com.example.domain.logic.chat

import com.google.gson.annotations.SerializedName

class RoomMsg {
    @SerializedName("room_id")
    var roomId = 0
    @SerializedName("content")
    var content: String = ""
    @SerializedName("from_uid")
    var sendUid: Int = 0
}