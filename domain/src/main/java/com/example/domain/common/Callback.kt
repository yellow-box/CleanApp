package com.example.domain.common

interface Callback<T> {
    fun call(data: T)
}

interface SinglePCallback<T> {
    fun call(data: T)
}