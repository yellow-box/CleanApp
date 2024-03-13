package com.example.domain.logic.chat

interface IChatRoomRepository {
    fun sendMsg(content: String, roomId: Int, uid: Int)

    fun registerNewMsgListener(l:NewMsgListener)

    fun loadOldMsg(roomId: Int):List<RoomMsg>

    fun clear()

    interface NewMsgListener{
        fun onReceiveNewMsg(roomMsg: RoomMsg)
    }
}