package com.example.domain.logic.chat

import com.example.domain.common.Callback

interface IChatRoomRepository {
    fun sendMsg(content: String, roomId: Int, uid: Int)

    fun registerNewMsgListener(l:NewMsgListener)

    fun loadOldMsg(roomId: Int,loadCallback: Callback<List<RoomMsg>>)

    fun clear()

    interface NewMsgListener{
        fun onReceiveNewMsg(roomMsg: RoomMsg)
    }

}