package com.example.domain.memostore

interface InMemoDataCallback<T> {
    fun onLoadSuccess(data:T)

    fun onLoadFailed(msg:String){
        println("load failed,$msg")
    }
}