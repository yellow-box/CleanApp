package com.example.cleanapp.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cleanapp.databinding.ActivityChatRoomBinding
import com.example.domain.ApiService
import com.example.domain.device.ILoginUser
import com.example.domain.device.IToast
import com.example.domain.logic.chat.VRoomMsg
import kotlinx.coroutines.launch

class ChatRoomActivity : AppCompatActivity(), IChatAction {
    private lateinit var binding: ActivityChatRoomBinding
    private lateinit var chatViewModel: ChatViewModel
    private val adapter: ChatAdapter = ChatAdapter()
    var roomId = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        roomId = intent.getIntExtra(ChatViewModel.PARAM_ROOM_ID, 0)
        chatViewModel = ViewModelProvider(this, ChatViewModelFactory())[ChatViewModel::class.java]
        initEvent()
    }

    private fun initEvent() {
        binding.chatContentRv.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
        binding.sendBtn.setOnClickListener {
            val msg = VRoomMsg()
            msg.roomId = roomId
            msg.sender = ApiService[ILoginUser::class.java]?.getUid()?:0
            msg.content = binding.sendEt.text.toString()
            binding.sendEt.setText("")
            chatViewModel.sendMsg(msg)
        }
        lifecycleScope.launch {
            chatViewModel.chatStateFlow.collect {
                when (it) {
                    is ChatState.RecNewMsg -> {
                        if (it.msg.sender == ApiService[ILoginUser::class.java]?.getUid()) {
                            showMyselfNewMsg(it.msg)
                        } else {
                            showNewOtherNewMsg(it.msg)
                        }
                    }

                    is ChatState.InitChatRoomSuccess -> {
                        showHistory(it.msgS)
                    }

                    is ChatState.Empty -> {}
                    else -> {}
                }
            }
        }
        chatViewModel.initChatRoom(roomId)
    }

    override fun showHistory(msgS: List<VRoomMsg>) {
        adapter.updateData(msgS)
    }

    override fun showMyselfNewMsg(msg: VRoomMsg) {
        adapter.addData(msg)
    }

    override fun showNewOtherNewMsg(msg: VRoomMsg) {
        adapter.addData(msg)
    }

    override fun showErrorMsg(msg: String) {
        ApiService[IToast::class.java]?.showToast(msg, 0)
    }
}