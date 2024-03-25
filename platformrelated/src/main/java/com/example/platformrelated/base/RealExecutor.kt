package com.example.platformrelated.base

import com.example.domain.socket.Executor
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class RealExecutor : Executor {
    private val mutex = Mutex()
    override fun runInMain(r: Runnable, delay: Long) {
        if (delay == 0L) {
            CoroutineScope(Dispatchers.Main + CoroutineName("RealExecutor mainExecutor")).launch {
                r.run()
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                delay(delay)
                withContext(Dispatchers.Main + CoroutineName("RealExecutor mainExecutor")) {
                    r.run()
                }
            }
        }
    }

    override fun runInChild(r: Runnable) {
        CoroutineScope(Dispatchers.IO + CoroutineName("RealExecutor childExecutor")).launch {
            r.run()
        }
    }

    override fun runInChildWithMutex(r: Runnable) {
        CoroutineScope(Dispatchers.IO + CoroutineName("RealExecutor childExecutor")).launch {
           mutex.withLock {
               r.run()
           }
        }
    }
}