package com.example.cleanapp.chat


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.logic.chat.ChatRoomRepository
import java.lang.Exception

class ChatViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(ChatRoomRepository()) as T
        }
        throw Exception("unexpected class")
    }
}