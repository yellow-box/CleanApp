package com.example.domain.db

import com.example.domain.Api
import com.example.domain.logic.chat.RoomMsg
import kotlinx.coroutines.flow.Flow

interface IChatMsgFetcher : Api {
    fun saveMsg(chatMsg: RoomMsg, callback: DbCallback<Any>?)

    fun loadRoomMsgS(roomId: Int): Flow<List<RoomMsg>>

    fun loadAllRoomMsg():Flow<List<RoomMsg>>

    fun deleteMsg(msgId: String, callback: DbCallback<Any>?)
}