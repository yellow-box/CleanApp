package com.example.domain.device

import com.example.domain.Api

/**
 * 用于向外暴露 全局上下文
 */
interface IGlobalContextProvider<C> :Api{
    fun getContext():C
}