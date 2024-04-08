package com.example.domain.device

import com.example.domain.Api

/**
 * 展示toast
 */
interface IToast:Api {
    fun showToast(content: String, type: Int)
}