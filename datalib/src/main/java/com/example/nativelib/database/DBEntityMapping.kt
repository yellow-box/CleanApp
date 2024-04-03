package com.example.nativelib.database

import com.example.domain.logic.chat.RoomMsg
import com.example.domain.logic.user.User
import com.example.nativelib.database.entity.DbChatMsg
import com.example.nativelib.database.entity.DbUser

object DBEntityMapping {
    fun dbMsgToMsg(dbChatMsg: DbChatMsg): RoomMsg {
        return RoomMsg().apply {
            msgId = dbChatMsg.msgId
            sendUid = dbChatMsg.senderUid
            content = dbChatMsg.content
            roomId = dbChatMsg.roomId
        }
    }

    fun roomMsgToDbMsg(roomMsg: RoomMsg): DbChatMsg {
        return DbChatMsg().apply {
            msgId = roomMsg.msgId
            senderUid = roomMsg.sendUid
            content = roomMsg.content
            roomId = roomMsg.roomId
        }
    }

    fun dbUserToUser(dbUser: DbUser): User {
        return User().apply {
            uid = dbUser.id
            name = dbUser.name
        }
    }

    fun userToDbUser(user: User): DbUser {
        return DbUser().apply {
            id = user.uid
            name = user.name
        }
    }
}