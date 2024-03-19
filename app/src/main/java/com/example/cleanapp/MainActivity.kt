package com.example.cleanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cleanapp.chat.ChatRoomActivity
import com.example.cleanapp.chat.ChatViewModel
import com.example.cleanapp.databinding.ActivityMainBinding
import com.example.datalib.TestJson
import com.example.domain.ApiService
import com.example.domain.logic.SocketInfo
import com.example.domain.socket.ILogicAction
import com.example.nativelib.NativeLib
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        println("rust enc version:${NativeLib().stringFromJNI()}")
        val gson = Gson()
        val jsonStr = gson.toJson(TestJson())
        println("TestJson:${jsonStr}")
        val tj = gson.fromJson(jsonStr, TestJson::class.java)
        println("unmarshell TestJson content:${tj.content} ")
    }
}