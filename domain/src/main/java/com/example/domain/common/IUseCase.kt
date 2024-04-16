package com.example.domain.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class IUseCase<out T, in P> {
    fun execute(param: P, callBack: UseCaseCallback<T?>?) {
        var isFail = false
        var exeCuteException: Exception? = null
        val job = CoroutineScope(Dispatchers.IO).async {
            try {
                run(param)
            } catch (e: Exception) {
                isFail = true
                exeCuteException = e
                null
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            if (!isFail) {
                callBack?.onResult(job.await())
            } else {
                callBack?.onFail(-1, "failed ,e =${exeCuteException.toString()}")
            }
        }
    }

    abstract  suspend fun run(param: P): T?

}

interface UseCaseCallback<in T> {
    fun onResult(data: T)

    fun onFail(code: Int, msg: String)
}

