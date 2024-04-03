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

class LoginUserDatasource : ILoginUser {
    private val inMemoStore: InMemoStore by lazy { ApiService[InMemoStore::class.java] }

    override fun getUid(callback: InMemoDataCallback<Int>) {
        inMemoStore.loadInt(KEY_LOGIN_USER_ID, object : InMemoDataCallback<Int?> {
            override fun onLoadSuccess(data: Int?) {
                callback.onLoadSuccess(data ?: INVALID_UID)
            }

            override fun onLoadFailed(msg: String) {
                callback.onLoadFailed(msg)
            }
        })
    }

    override fun getName(callback: InMemoDataCallback<String?>) {
        inMemoStore.loadInt(KEY_LOGIN_USER_ID, object : InMemoDataCallback<Int?> {
            override fun onLoadSuccess(data: Int?) {
                data?.let {
                    UserManager.get(it, object : LoadUserCallback {
                        override fun onLoadUserSuccess(u: User) {
                            callback.onLoadSuccess(u.name)
                        }

                        override fun onLoadFail(msg: String) {
                            callback.onLoadFailed(msg)
                        }

                    })
                }
            }

            override fun onLoadFailed(msg: String) {
                callback.onLoadFailed(msg)
            }
        })
    }

    override fun login(uid: Int) {
        inMemoStore.save(KEY_LOGIN_USER_ID, uid)
    }

    override fun logOut() {
        inMemoStore.save(KEY_LOGIN_USER_ID, INVALID_UID)
    }

}