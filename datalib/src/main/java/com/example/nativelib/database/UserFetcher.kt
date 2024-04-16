package com.example.nativelib.database

import android.content.Context
import com.example.domain.ApiService
import com.example.domain.db.DbCallback
import com.example.domain.db.IUserFetcher
import com.example.domain.device.IGlobalContextProvider
import com.example.domain.logic.user.User
import com.example.nativelib.database.db.CleanDatabase
import com.example.nativelib.database.entity.DbUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserFetcher : IUserFetcher {
    private val db: CleanDatabase by lazy {
        val context = ApiService[IGlobalContextProvider::class.java].getContext() as Context
        CleanDatabase.getDataBase(context)
    }

    override fun saveUser(user: User, callback: DbCallback<Any>?) {
        withCatch(callback) {
            db.userDao().insertNewUse(DBEntityMapping.userToDbUser(user))
            Any()
        }
    }

    override fun loadUser(uid: Int): Flow<User?> {
        return db.userDao().queryUser(uid).map { value: DbUser? ->
            value?.run {
                DBEntityMapping.dbUserToUser(this)
            }
        }
    }

    override fun loadAllUsers(): Flow<List<User>> {
        return db.userDao().queryAllUser()
            .map { users -> users.map { DBEntityMapping.dbUserToUser(it) } }
    }

    override fun removeUser(uid: Int, callback: DbCallback<Any>?) {
        withCatch(callback) {
            db.userDao().remove(uid)
        }
    }
}