package com.example.domain.logic.user

import com.example.domain.ApiService
import com.example.domain.db.DbCallback
import com.example.domain.db.IUserFetcher


object UserManager {
    private val userS: MutableMap<Int, User> = HashMap()
    const val SystemUid = 1

    init {
        ApiService[IUserFetcher::class.java].loadAllUsers(object : DbCallback<List<User>> {
            override fun onSuccess(data: List<User>) {
                loadAllFromDb(data)
            }

            override fun onFail(nsg: String) {
                println("UserManager load AllUser failed,$nsg")
            }

        })
    }

    private fun loadAllFromDb(dbUsers: List<User>) {
        dbUsers.forEach {
            userS[it.uid] = it
        }
    }

    fun updateUser(user: User) {
        userS[user.uid] = user
        ApiService[IUserFetcher::class.java].saveUser(user, null)
    }

    fun get(uid: Int, callback: LoadUserCallback) {
        val memUser = userS[uid]
        if (memUser == null) {
            ApiService[IUserFetcher::class.java].loadUser(uid, object : DbCallback<User?> {
                override fun onSuccess(data: User?) {
                    data?.let {
                        userS[it.uid] = it
                        callback.onLoadUserSuccess(it)
                    }
                    if (data == null) {
                        callback.onLoadFail("no such user")
                    }
                }

                override fun onFail(nsg: String) {
                    callback.onLoadFail(nsg)
                }

            })
        } else {
            callback.onLoadUserSuccess(memUser)
        }
    }
}

interface LoadUserCallback {
    fun onLoadUserSuccess(u: User)

    fun onLoadFail(msg: String)
}