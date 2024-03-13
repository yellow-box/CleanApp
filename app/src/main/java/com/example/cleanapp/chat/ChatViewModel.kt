package com.example.cleanapp.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.logic.chat.ChatModelMapping
import com.example.domain.logic.chat.IChatRoomRepository
import com.example.domain.logic.chat.RoomMsg
import com.example.domain.logic.chat.VRoomMsg
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

    init {
        chatRoomRepository.registerNewMsgListener(object : IChatRoomRepository.NewMsgListener {
            override fun onReceiveNewMsg(roomMsg: RoomMsg) {
                viewModelScope.launch {
                    _chatStateFlow.emit(ChatState.RecNewMsg(roomId, ChatModelMapping.toViewModel(roomMsg)))
                }
            }
        })
    }

    fun initChatRoom(roomId: Int) {
        viewModelScope.launch {
            _chatStateFlow.emit(ChatState.InitChatRoomSuccess(roomId, listOf()))
        }
    }

    fun sendMsg(msg: VRoomMsg) {
        viewModelScope.launch {
            chatRoomRepository.sendMsg(msg.content, msg.roomId, msg.sender)
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