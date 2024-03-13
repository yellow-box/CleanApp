package com.example.domain.logic.user

import java.util.concurrent.CopyOnWriteArrayList

object UserManager {
    private val userS: MutableList<User> = CopyOnWriteArrayList()
    const val SystemUid = 1
    fun updateUser(user: User) {
        val curUser = this[user.uid]
        if (curUser == null) {
            userS.add(user)
        } else {
            curUser.name = user.name
        }
    }

    operator fun get(uid: Int): User? {
        return userS.firstOrNull {
            it.uid == uid
        }
    }
}