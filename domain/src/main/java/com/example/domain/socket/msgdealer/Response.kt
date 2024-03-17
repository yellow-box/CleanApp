package com.example.domain.socket.msgdealer

import com.google.gson.annotations.SerializedName

class Response(
    @SerializedName("code")
    val code: Int,
    @SerializedName("msg")
    val msg: String
)

