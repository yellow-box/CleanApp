package com.example.cleanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.example.cleanapp.chat.ChatRoomActivity
import com.example.cleanapp.chat.ChatViewModel
import com.example.cleanapp.databinding.ActivityMainBinding
import com.example.datalib.socket.ChatSocket
import com.example.domain.ApiService
import com.example.domain.device.ILoginUser
import com.example.domain.device.IToast
import com.example.domain.logic.SocketInfo
import com.example.domain.socket.ILogicAction
import com.example.nativelib.NativeSocketProxy
import com.example.platformrelated.base.RealExecutor

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val JAVA_SOCKET = "java_socket"
    private val C_SOCKET = "c_socket"
    private val socketItems = arrayListOf(JAVA_SOCKET,C_SOCKET,"none")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initEvent()
    }

    private fun initView() {
        binding.userIdEt.setText("${(ApiService[ILoginUser::class.java]?.getUid() ?: -1)}")
        val  spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,socketItems)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.socketSpinner.adapter = spinnerAdapter;
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
        binding.socketSpinner.onItemSelectedListener = object:OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>, p1: View?, p2: Int, p3: Long) {
               when(p0.getItemAtPosition(p2)){
                   JAVA_SOCKET->{
                       ApiService[ILogicAction::class.java]?.apply {
                           disconnect()
                           initSetting(ChatSocket(),RealExecutor())
                       }
                   }

                   C_SOCKET-> {
                       ApiService[ILogicAction::class.java]?.apply {
                           disconnect()
                           initSetting(NativeSocketProxy() ,RealExecutor())
                       }
                   }

                   else->{
                       ApiService[IToast::class.java]?.showToast("do nothing",0)
                   }
               }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }
}