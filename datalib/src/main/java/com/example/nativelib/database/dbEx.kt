package com.example.nativelib.database

import android.os.Looper
import com.example.domain.ApiService
import com.example.domain.db.DbCallback
import com.example.domain.socket.Executor

inline fun <T> withCatch(callback: DbCallback<T>?, crossinline block: () -> T) {
    val isMainThread = Looper.getMainLooper() == Looper.myLooper()
    val executor = ApiService[Executor::class.java]
    executor.runInChild {
        try {
            val data = block.invoke()
            callback?.let { successInvoke(it, isMainThread, executor, data) }
        } catch (e: Exception) {
            callback?.let {
                failInvoke(it, isMainThread, executor, e)
            }
        }
    }
}

inline fun <T> successInvoke(
    callback: DbCallback<T>,
    runInMain: Boolean,
    executor: Executor,
    data: T
) {
    if (runInMain) {
        executor.runInMain({
            callback.onSuccess(data)
        }, 0)
    } else {
        executor.runInChild {
            callback.onSuccess(data)
        }
    }
}

inline fun <T> failInvoke(
    callback: DbCallback<T>,
    runInMain: Boolean,
    executor: Executor,
    e: Exception?
) {
    if (runInMain) {
        callback.let {
            executor.runInMain({
                it.onFail("fail e=${e.toString()}")
            }, 0)
        }
    } else {
        callback.let {
            executor.runInChild {
                it.onFail("fail e=${e.toString()}")
            }
        }
    }
}