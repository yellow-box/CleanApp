package com.example.domain

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

object ApiService {

    private val implMap: MutableMap<Class<*>, Api> = ConcurrentHashMap()

    operator fun <T : Api> get(clazz: Class<T>): T {
        val api = implMap[clazz]
        return (api ?: {
            Proxy.newProxyInstance(
                clazz.classLoader, arrayOf(clazz), NopInvocationHandler()
            ) as T
        }) as T
    }

    fun <T : Api> register(clazz: Class<T>, impl: T) {
        implMap[clazz] = impl
    }
}

class NopInvocationHandler : InvocationHandler {
    override fun invoke(p0: Any?, p1: Method?, p2: Array<out Any>?): Any {
        println("ApiService invoke Nop handler")
        return Any()
    }

}