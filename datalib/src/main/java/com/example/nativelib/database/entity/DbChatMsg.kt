package com.example.nativelib.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ChatMsg")
class DbChatMsg {
    @PrimaryKey
    @ColumnInfo(name ="msgId")
    var msgId:String = ""
    @ColumnInfo(name ="roomId")
    var roomId:Int = 0
    @ColumnInfo(name ="content")
    var content:Int = 0
    @ColumnInfo(name ="senderUid")
    var senderUid:Int = 0
}