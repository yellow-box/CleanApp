package com.example.domain.memostore

import com.example.domain.Api

interface InMemoStore : Api {
    fun initSetting()
    fun save(key: String, value: String)

    fun loadString(key: String, callback: InMemoDataCallback<String?>)

    fun save(key: String, value: Int)

    fun loadInt(key: String, callback: InMemoDataCallback<Int?>)
}