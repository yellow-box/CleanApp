package com.example.cleanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cleanapp.chat.ChatRoomActivity
import com.example.cleanapp.chat.ChatViewModel
import com.example.cleanapp.databinding.ActivityMainBinding
import com.example.domain.ApiService
import com.example.domain.device.ILoginUser
import com.example.domain.logic.SocketInfo
import com.example.domain.socket.ILogicAction

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initEvent()
    }

    private fun initView() {
        binding.userIdEt.setText("${(ApiService[ILoginUser::class.java]?.getUid() ?: -1)}")
    }

    private fun initEvent() {
        binding.jumpBtn.setOnClickListener {
            with(Intent(this, ChatRoomActivity::class.java)) {
                putExtra(ChatViewModel.PARAM_ROOM_ID, 1)
                startActivity(this)
            }
        }
        binding.connBtn.setOnClickListener {
            ApiService[ILogicAction::class.java]?.connect(SocketInfo.ip, SocketInfo.port)
        }
        binding.disconnBtn.setOnClickListener {
            ApiService[ILogicAction::class.java]?.disconnect()
        }
        binding.userIdCheckBtn.setOnClickListener {
            ApiService[ILoginUser::class.java]?.saveUid((binding.userIdEt.text).toString().toInt())
            binding.userIdEt.setText("${(ApiService[ILoginUser::class.java]?.getUid() ?: -1)}")
        }
    }
}