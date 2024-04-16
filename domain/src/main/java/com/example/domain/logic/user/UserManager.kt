package com.example.domain.logic.user

import com.example.domain.ApiService
import com.example.domain.db.DbCallback
import com.example.domain.db.IUserFetcher
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean


/**
 * 所有的用户信息管理
 */
object UserManager {
    private val userS: MutableMap<Int, User> = HashMap()
    const val SystemUid = 1

    private fun loadAllFromDb(dbUsers: List<User>) {
        dbUsers.forEach {
            userS[it.uid] = it
        }
    }

    fun updateUser(user: User) {
        userS[user.uid] = user
        ApiService[IUserFetcher::class.java].saveUser(user, null)
    }

    fun get(uid: Int): Flow<User?> {
        val memUser = userS[uid]
        return if (memUser == null) {
            val deferred = CompletableDeferred<Boolean>()
            CoroutineScope(Dispatchers.IO).launch {
                ApiService[IUserFetcher::class.java].loadUser(uid).collect {
                    it?.let {
                        userS[it.uid] = it
                    }
                    deferred.complete(true)
                }
            }

            flow {
                deferred.await()
                emit(userS[uid])
            }.flowOn(Dispatchers.IO)
        } else {
            flow<User> {
                emit(memUser)
            }.flowOn(Dispatchers.IO)
        }
    }
}

interface LoadUserCallback {
    fun onLoadUserSuccess(u: User)

    fun onLoadFail(msg: String)
}