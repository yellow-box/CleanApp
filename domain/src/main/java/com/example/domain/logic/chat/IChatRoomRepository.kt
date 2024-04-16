package com.example.domain.logic.chat

import com.example.domain.common.Callback
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IChatRoomRepository {
    val newMsgFlow:Flow<RoomMsg>
    fun sendMsg(content: String, roomId: Int, uid: Int)


    fun loadOldMsg(roomId: Int): Flow<List<RoomMsg>>

    fun clear()

    interface NewMsgListener{
        fun onReceiveNewMsg(roomMsg: RoomMsg)
    }

}