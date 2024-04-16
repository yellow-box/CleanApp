package com.example.domain.device

import com.example.domain.Api
import kotlinx.coroutines.flow.Flow

/**
 * 已登录用户的信息管理
 */
interface ILoginUser : Api {
    fun getUid(): Flow<Int>
    fun getName(): Flow<String>


    fun login(uid: Int)

    fun logOut()
}