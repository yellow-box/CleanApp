package com.example.cleanapp.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.ApiService
import com.example.domain.common.UseCaseCallback
import com.example.domain.device.IToast
import com.example.domain.logic.chat.ChatModelMapping
import com.example.domain.logic.chat.IChatRoomRepository
import com.example.domain.logic.chat.usecase.LoadRoomMsgSUseCase
import com.example.domain.logic.chat.RoomMsg
import com.example.domain.logic.chat.RoomMsgManager
import com.example.domain.logic.chat.usecase.SendMsgUseCase
import com.example.domain.logic.chat.VRoomMsg
import com.example.domain.logic.chat.usecase.ReceiveNewMsgUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val chatRoomRepository: IChatRoomRepository) : ViewModel() {
    companion object {
        const val PARAM_ROOM_ID = "ROOM_ID"
    }

    var roomId = 0
    private val _chatStateFlow: MutableStateFlow<ChatState> = MutableStateFlow(
        ChatState.Empty(
            roomId
        )
    )
    val chatStateFlow = _chatStateFlow as StateFlow<ChatState>
    private val sendMsgUseCase = SendMsgUseCase(chatRoomRepository)
    private val loadMsgUseCase = LoadRoomMsgSUseCase(chatRoomRepository)
    private val receiveNewMsgUseCase = ReceiveNewMsgUseCase(chatRoomRepository)

    init {
        receiveNewMsgUseCase.execute(0, object : UseCaseCallback<Flow<RoomMsg>?> {
            override fun onResult(data: Flow<RoomMsg>?) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModelScope.launch {
                        data?.collect {
                            _chatStateFlow.emit(
                                ChatState.RecNewMsg(
                                    roomId,
                                    ChatModelMapping.toViewModel(it)
                                )
                            )
                        }
                    }
                }
            }

            override fun onFail(code: Int, msg: String) {
                ApiService[IToast::class.java].showToast(msg, 0)
            }
        })
    }

    fun initChatRoom(roomId: Int) {
        loadMsgUseCase.execute(roomId, object : UseCaseCallback<Flow<List<RoomMsg>>?> {
            override fun onResult(data: Flow<List<RoomMsg>>?) {
                viewModelScope.launch {
                    data?.collect {
                        _chatStateFlow.emit(
                            ChatState.InitChatRoomSuccess(
                                roomId,
                                ChatModelMapping.toViewModels(it)
                            )
                        )
                    }
                }
            }

            override fun onFail(code: Int, msg: String) {
                ApiService[IToast::class.java].showToast(msg, 0)
            }
        })
    }

    fun sendMsg(msg: VRoomMsg) {
        viewModelScope.launch {
            sendMsgUseCase.execute(ChatModelMapping.toDataModel(msg), null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatRoomRepository.clear()
    }
}

open class ChatState {
    data class Empty(val roomId: Int) : ChatState()
    data class SendMsgSuccess(val roomId: Int, val msg: String) : ChatState()
    data class SendMsgFailed(val roomId: Int, val msg: String) : ChatState()
    data class RecNewMsg(val roomId: Int, val msg: VRoomMsg) : ChatState()
    data class InitChatRoomSuccess(val roomId: Int, val msgS: List<VRoomMsg>) : ChatState()
    data class InitChatRoomFailed(val roomId: Int, val msgS: List<VRoomMsg>, val failMsg: String) :
        ChatState()
}