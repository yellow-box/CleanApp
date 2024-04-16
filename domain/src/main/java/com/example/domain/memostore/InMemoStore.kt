package com.example.domain.memostore

import com.example.domain.Api
import kotlinx.coroutines.flow.Flow

/**
 * 内存中键值对 信息的存储抽象
 */
interface InMemoStore : Api {
    fun initSetting()
    fun save(key: String, value: Int)
    fun save(key: String, value: String)

    fun loadString(key: String, defaultValue: String): Flow<String>

    fun loadInt(key: String, defaultValue: Int): Flow<Int>

}