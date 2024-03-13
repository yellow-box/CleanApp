package com.example.domain.device

import com.example.domain.Api

interface IGlobalContextProvider<C> :Api{
    fun getContext():C
}