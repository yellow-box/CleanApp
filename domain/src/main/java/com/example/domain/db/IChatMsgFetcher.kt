package com.example.domain.db

import com.example.domain.Api
import com.example.domain.logic.chat.RoomMsg

interface IChatMsgFetcher : Api {
    fun saveMsg(chatMsg: RoomMsg, callback: DbCallback<Any>?)

    fun loadRoomMsgS(roomId: Int, callback: DbCallback<List<RoomMsg>>?)

    fun loadAllRoomMsg(callback: DbCallback<List<RoomMsg>>?)

    fun deleteMsg(msgId: String, callback: DbCallback<Any>?)
}