package com.example.domain.device

import com.example.domain.Api

interface ILoginUser:Api {
    fun getUid():Int
    fun getName():String

    fun saveUid(uid: Int)

    fun saveName(name:String)
}