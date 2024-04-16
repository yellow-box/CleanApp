package com.example.domain.user

import com.example.domain.ApiService
import com.example.domain.device.ILoginUser
import com.example.domain.logic.user.LoadUserCallback
import com.example.domain.logic.user.User
import com.example.domain.logic.user.UserManager
import com.example.domain.memostore.INVALID_UID
import com.example.domain.memostore.InMemoDataCallback
import com.example.domain.memostore.InMemoStore
import com.example.domain.memostore.KEY_LOGIN_USER_ID
import kotlinx.coroutines.flow.Flow

class LoginUserDatasource : ILoginUser {
    private val inMemoStore: InMemoStore by lazy { ApiService[InMemoStore::class.java] }

    override fun getUid(): Flow<Int> {
        return inMemoStore.loadInt(KEY_LOGIN_USER_ID, INVALID_UID)
    }

    override fun getName(): Flow<String> {
        return inMemoStore.loadString(KEY_LOGIN_USER_ID, "no_name")
    }

    override fun login(uid: Int) {
        inMemoStore.save(KEY_LOGIN_USER_ID, uid)
    }

    override fun logOut() {
        inMemoStore.save(KEY_LOGIN_USER_ID, INVALID_UID)
    }

}