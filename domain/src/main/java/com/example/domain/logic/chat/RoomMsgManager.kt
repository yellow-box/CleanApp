package com.example.domain.logic.chat

import com.example.domain.ApiService
import com.example.domain.db.DbCallback
import com.example.domain.db.IChatMsgFetcher
import com.example.domain.socket.msgdealer.MainRouter
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean


/**
 * 房间消息管理
 */
class RoomMsgManager : IChatRoomRepository.NewMsgListener {
    private val _pushMsgFlow = MutableSharedFlow<RoomMsg>()
    val pushMsgFlow = _pushMsgFlow.asSharedFlow()

    init {
        startListenRoomPushMsg()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun startListenRoomPushMsg() {
        GlobalScope.launch(Dispatchers.IO) {
            MainRouter.instance.roomPushMsgSharedFlow?.collect {
                saveMsg(it)
                _pushMsgFlow.emit(it)
            }
        }
    }

    companion object {
        private val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { RoomMsgManager() }
        fun getManger(): RoomMsgManager {
            return instance
        }
    }


    private fun saveMsg(roomMsg: RoomMsg) {
        ApiService[IChatMsgFetcher::class.java].saveMsg(roomMsg, null)
    }

    override fun onReceiveNewMsg(roomMsg: RoomMsg) {
        saveMsg(roomMsg)
        CoroutineScope(Dispatchers.IO).launch {
            _pushMsgFlow.emit(roomMsg)
        }
    }
}