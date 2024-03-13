package com.example.domain.device

import com.example.domain.Api

interface IToast:Api {
    fun showToast(content: String, type: Int)
}