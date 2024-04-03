package com.example.domain.device

import com.example.domain.Api
import com.example.domain.memostore.InMemoDataCallback

interface ILoginUser:Api {
    fun getUid(callback: InMemoDataCallback<Int>)
    fun getName(callback: InMemoDataCallback<String?>)


    fun login(uid:Int)

    fun logOut()
}