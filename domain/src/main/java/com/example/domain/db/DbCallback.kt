package com.example.domain.db


interface DbCallback<T> {
    fun onSuccess(data: T)

    fun onFail(nsg: String)
}



