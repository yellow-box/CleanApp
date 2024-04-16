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
import com.example.domain.memostore.INVALID_UID
import com.example.domain.memostore.InMemoDataCallback
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatRoomActivity : AppCompatActivity(), IChatAction {
    private lateinit var binding: ActivityChatRoomBinding
    private lateinit var chatViewModel: ChatViewModel
    private val adapter: ChatAdapter = ChatAdapter()
    var roomId = 1
    var loginUid = INVALID_UID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        roomId = intent.getIntExtra(ChatViewModel.PARAM_ROOM_ID, 0)
        binding.roomTv.text = "$roomId"
        chatViewModel = ViewModelProvider(this, ChatViewModelFactory())[ChatViewModel::class.java]
        initEvent()
    }

    private fun initEvent() {
        lifecycleScope.launch {
            ApiService[ILoginUser::class.java].getUid().collect {
                loginUid = it
            }
        }
        binding.chatContentRv.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
        binding.sendBtn.setOnClickListener {
            val msg = VRoomMsg()
            msg.roomId = roomId
            msg.sender = loginUid
            msg.content = binding.sendEt.text.toString()
            binding.sendEt.setText("")
            chatViewModel.sendMsg(msg)
        }

        lifecycleScope.launch {
            chatViewModel.chatStateFlow.collect {
                when (it) {
                    is ChatState.RecNewMsg -> {
                        if (it.msg.sender == loginUid) {
                            showMyselfNewMsg(it.msg)
                            scrollToLatest()
                        } else {
                            showNewOtherNewMsg(it.msg)
                            scrollToLatest()
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

    private fun scrollToLatest() {
        binding.chatContentRv.smoothScrollToPosition(adapter.itemCount)
    }

    override fun showErrorMsg(msg: String) {
        ApiService[IToast::class.java].showToast(msg, 0)
    }
}