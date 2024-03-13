package com.example.domain

import java.util.concurrent.ConcurrentHashMap

object ApiService {

    private val implMap: MutableMap<Class<*>, Api> = ConcurrentHashMap()

    operator fun <T : Api> get(api: Class<T>): T? {
        return implMap[api] as T
    }

    fun <T : Api> register(clazz: Class<T>, impl: T) {
        implMap[clazz] = impl
    }
}