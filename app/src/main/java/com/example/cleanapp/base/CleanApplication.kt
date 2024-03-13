package com.example.cleanapp.base

import android.app.Application
import android.content.Context
import com.example.datalib.database.LoginUserDatasource
import com.example.datalib.socket.ChatSocket
import com.example.domain.ApiService
import com.example.domain.device.IGlobalContextProvider
import com.example.domain.device.ILoginUser
import com.example.domain.device.IToast
import com.example.domain.logic.SocketInfo
import com.example.domain.socket.ILogicAction
import com.example.domain.socket.ISocket
import com.example.domain.socket.ISocketMsgDealer
import com.example.domain.socket.SocketManager
import com.example.platformrelated.base.RealExecutor
import com.example.platformrelated.base.ToastHelper

class CleanApplication : Application(), IGlobalContextProvider<Context> {
    companion object {
        lateinit var app: CleanApplication
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        initStuff()
    }

    private fun initStuff() {
        ApiService.register(IGlobalContextProvider::class.java, this)
        ApiService.register(IToast::class.java, ToastHelper())
        ApiService.register(ILogicAction::class.java, SocketManager())
        ApiService.register(ILoginUser::class.java, LoginUserDatasource())
        connectSocket()
    }

    private fun connectSocket() {
        val socketManger = ApiService[ILogicAction::class.java] ?: return
        socketManger.initSetting(ChatSocket(), RealExecutor())
        socketManger.connect(SocketInfo.ip, SocketInfo.port)
    }

    override fun getContext(): Context {
        return app
    }

}