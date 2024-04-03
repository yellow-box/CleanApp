package com.example.nativelib.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import com.example.nativelib.database.entity.DbUser

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNewUse(u: DbUser)

    @Update
    fun updateUser(u: DbUser)

    @Query("select * from DbUser where :uid = id")
    fun queryUser(uid: Int): DbUser

    @Query("select * from DbUser")
    fun queryAllUser(): List<DbUser>

    @Query("delete from DbUser where :uid == id ")
    fun remove(uid: Int)
}