package com.example.domain.base

import com.google.gson.Gson


object GsonUtil {
     val gson: Gson by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Gson() }
}