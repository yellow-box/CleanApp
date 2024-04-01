package com.example.nativelib.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.nativelib.database.entity.DbChatMsg

@Dao
interface ChatMsgDao {
    @Insert
    fun insertChatMsg(chatMsg: DbChatMsg)

    @Query("select * from ChatMsg where :roomId==roomId")
    fun queryMsgSByRoomId(roomId:String):List<DbChatMsg>

    @Delete
    fun deleteChatMsg(msgId:String)
}