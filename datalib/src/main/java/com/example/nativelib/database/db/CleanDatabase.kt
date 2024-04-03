package com.example.nativelib.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.domain.logic.user.User
import com.example.nativelib.database.dao.ChatMsgDao
import com.example.nativelib.database.dao.UserDao
import com.example.nativelib.database.entity.DbChatMsg
import com.example.nativelib.database.entity.DbUser

@Database(entities = [DbUser::class, DbChatMsg::class], version = 1)
abstract class CleanDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun chatMsgDao(): ChatMsgDao

    companion object {
        @Volatile
        private var instance: CleanDatabase? = null
        fun getDataBase(context: Context): CleanDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, CleanDatabase::class.java, "cleanApp_database")
                    .build().also {
                        instance = it
                    }
            }
        }

    }
}