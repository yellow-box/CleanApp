package com.example.domain.socket

interface Executor {
    fun runInMain(r: Runnable, delay: Long)
    fun runInChild(r: Runnable)

    //互斥访问 执行 r.run
    fun runInChildWithMutex(r: Runnable)
}