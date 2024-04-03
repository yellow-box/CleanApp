package com.example.domain.db

import com.example.domain.Api
import com.example.domain.logic.user.User

interface IUserFetcher : Api {
    fun saveUser(user: User, callback: DbCallback<Any>?)

    fun loadUser(uid: Int, callback: DbCallback<User?>?)

    fun loadAllUsers(callback: DbCallback<List<User>>?)

    fun removeUser(uid: Int, callback: DbCallback<Any>?)
}