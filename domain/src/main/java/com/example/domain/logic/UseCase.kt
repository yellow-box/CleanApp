package com.example.domain.logic

interface UserCase<D,R> {
    interface UserCaseCallback <R>{
        fun onSuccess(msg: R)

        fun onFail(msg: String, t: Throwable?)
    }

    fun execute(data: D, callback: UserCaseCallback<R>)
}