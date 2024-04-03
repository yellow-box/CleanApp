package com.example.nativelib.database

import android.content.Context
import com.example.domain.ApiService
import com.example.domain.db.DbCallback
import com.example.domain.db.IChatMsgFetcher
import com.example.domain.device.IGlobalContextProvider
import com.example.domain.logic.chat.RoomMsg
import com.example.nativelib.database.db.CleanDatabase

class ChatMsgFetcher : IChatMsgFetcher {
    private val db: CleanDatabase by lazy {
        val context = ApiService[IGlobalContextProvider::class.java].getContext() as Context
        CleanDatabase.getDataBase(context)
    }

    override fun saveMsg(chatMsg: RoomMsg, callback: DbCallback<Any>?) {
        withCatch(callback) {
            db.chatMsgDao().insertChatMsg(DBEntityMapping.roomMsgToDbMsg(chatMsg))
            Any()
        }
    }

    override fun loadRoomMsgS(roomId: Int, callback: DbCallback<List<RoomMsg>>?) {
        withCatch(callback) {
            db.chatMsgDao().queryMsgSByRoomId(roomId).map { DBEntityMapping.dbMsgToMsg(it) }
        }
    }

    override fun loadAllRoomMsg(callback: DbCallback<List<RoomMsg>>?) {
        withCatch(callback) {
            db.chatMsgDao().queryAllMsgS().map { DBEntityMapping.dbMsgToMsg(it) }
        }
    }

    override fun deleteMsg(msgId: String, callback: DbCallback<Any>?) {
        withCatch(callback) {
            db.chatMsgDao().deleteChatMsg(msgId)
            Any()
        }
    }

}