package com.example.domain.db

import com.example.domain.Api
import com.example.domain.logic.user.User
import kotlinx.coroutines.flow.Flow

interface IUserFetcher : Api {
    fun saveUser(user: User, callback: DbCallback<Any>?)

    fun loadUser(uid: Int): Flow<User?>

    fun loadAllUsers():Flow<List<User>>

    fun removeUser(uid: Int, callback: DbCallback<Any>?)
}