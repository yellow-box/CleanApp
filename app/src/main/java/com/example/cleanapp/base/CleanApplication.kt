package com.example.cleanapp.base

import android.app.Application
import android.content.Context
import com.example.domain.user.LoginUserDatasource
import com.example.domain.ApiService
import com.example.domain.db.IChatMsgFetcher
import com.example.domain.db.IUserFetcher
import com.example.domain.device.IGlobalContextProvider
import com.example.domain.device.ILoginUser
import com.example.domain.device.IToast
import com.example.domain.logic.SocketInfo
import com.example.domain.memostore.InMemoStore
import com.example.domain.socket.Executor
import com.example.domain.socket.ILogicAction
import com.example.domain.socket.RawDataOperator
import com.example.domain.socket.SocketManager
import com.example.domain.socket.msgdealer.RawDataOperatorImpl
import com.example.nativelib.NativeSocketProxy
import com.example.nativelib.database.ChatMsgFetcher
import com.example.nativelib.database.UserFetcher
import com.example.nativelib.memorystore.MemoStoreImpl
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
        platformRelatedRegister()
        socketRegister()
        inMemoryStoreRegister()
        dbFetcherRegister()
        connectSocket()
    }

    private fun connectSocket() {
        val socketManger = ApiService[ILogicAction::class.java]
        socketManger.initSetting(NativeSocketProxy(), RealExecutor())
        socketManger.connect(SocketInfo.ip, SocketInfo.port)
    }

    override fun getContext(): Context {
        return app
    }

    private fun socketRegister() {
        ApiService.register(ILogicAction::class.java, SocketManager())
        ApiService.register(ILoginUser::class.java, LoginUserDatasource())
        ApiService.register(RawDataOperator::class.java, RawDataOperatorImpl())
    }

    private fun platformRelatedRegister() {
        ApiService.register(IGlobalContextProvider::class.java, this)
        ApiService.register(IToast::class.java, ToastHelper())
        ApiService.register(Executor::class.java, RealExecutor())
    }

    private fun dbFetcherRegister() {
        ApiService.register(IUserFetcher::class.java, UserFetcher())
        ApiService.register(IChatMsgFetcher::class.java, ChatMsgFetcher())
    }

    private fun inMemoryStoreRegister() {
        ApiService.register(InMemoStore::class.java, MemoStoreImpl())
    }

}