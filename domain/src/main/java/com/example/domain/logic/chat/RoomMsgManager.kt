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
    val msgS: MutableMap<Int, MutableList<RoomMsg>> = ConcurrentHashMap()
    private var hasInit = AtomicBoolean(false)
    private val _pushMsgFlow = MutableSharedFlow<RoomMsg>()
    val pushMsgFlow = _pushMsgFlow.asSharedFlow()
    private val deferred = CompletableDeferred<Boolean>()

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

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadDb(): CompletableDeferred<Boolean> {
        GlobalScope.launch(Dispatchers.IO) {
            if (!hasInit.get()) {
                ApiService[IChatMsgFetcher::class.java].loadAllRoomMsg().collect {
                    loadAllRoomMsgFromDb(it)
                    hasInit.set(true)
                    deferred.complete(true)
                }
            } else {
                deferred.complete(true)
            }
        }
        return deferred
    }

    companion object {
        private val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { RoomMsgManager() }
        fun getManger(): RoomMsgManager {
            return instance
        }
    }

    private fun addRoomMsg(roomMsg: RoomMsg) {
        val roomId = roomMsg.roomId
        if (!msgS.containsKey(roomId)) {
            msgS[roomId] = LinkedList()
            msgS[roomId]!!.add(roomMsg)
        } else {
            msgS[roomId]?.add(roomMsg)
        }
    }

    private fun loadAllRoomMsgFromDb(msgS: List<RoomMsg>) {
        msgS.forEach {
            addRoomMsg(it)
        }
    }

    fun getMsgSByRoomId(roomId: Int): Flow<List<RoomMsg>> {
        return if (hasInit.get()) {
            flow {
                emit(msgS[roomId] ?: emptyList())
            }
        } else {
            flow {
                val deferred = loadDb()
                deferred.await()
                emit(msgS[roomId] ?: emptyList())
            }
        }
    }

    private fun saveMsg(roomMsg: RoomMsg) {
        addRoomMsg(roomMsg)
        ApiService[IChatMsgFetcher::class.java].saveMsg(roomMsg, null)
    }

    override fun onReceiveNewMsg(roomMsg: RoomMsg) {
        saveMsg(roomMsg)
        CoroutineScope(Dispatchers.IO).launch {
            _pushMsgFlow.emit(roomMsg)
        }
    }
}

interface PushMsgListener {
    fun onPushMsg(roomMsg: RoomMsg)
}