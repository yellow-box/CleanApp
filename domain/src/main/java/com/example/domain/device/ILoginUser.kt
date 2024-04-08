package com.example.domain.device

import com.example.domain.Api
import com.example.domain.memostore.InMemoDataCallback

/**
 * 已登录用户的信息管理
 */
interface ILoginUser:Api {
    fun getUid(callback: InMemoDataCallback<Int>)
    fun getName(callback: InMemoDataCallback<String?>)


    fun login(uid:Int)

    fun logOut()
}