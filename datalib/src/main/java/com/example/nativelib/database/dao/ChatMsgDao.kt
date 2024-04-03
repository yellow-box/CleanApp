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

    @Query("select * from DbChatMsg where :roomId==roomId")
    fun queryMsgSByRoomId(roomId:Int):List<DbChatMsg>

    @Query("select * from DbChatMsg")
    fun queryAllMsgS():List<DbChatMsg>

    @Query("delete from DbChatMsg where :msgId==msgId")
    fun deleteChatMsg(msgId:String)
}