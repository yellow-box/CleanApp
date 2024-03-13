package com.example.platformrelated.base

import android.content.Context
import android.widget.Toast
import com.example.domain.ApiService
import com.example.domain.device.IGlobalContextProvider
import com.example.domain.device.IToast

class ToastHelper : IToast {
    override fun showToast(content: String, type: Int) {
        val context: Context =
            ApiService[IGlobalContextProvider::class.java]?.getContext() as Context
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }
}