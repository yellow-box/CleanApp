package com.example.domain.socket.msgdealer

import com.google.gson.annotations.SerializedName

class BindUserData(
    @SerializedName("uid")
    val uid: Int
)