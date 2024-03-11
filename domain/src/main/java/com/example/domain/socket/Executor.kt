package com.example.domain.socket

interface Executor {
    fun runInMain(r: Runnable, delay: Long)
    fun runInChild(r: Runnable)
}