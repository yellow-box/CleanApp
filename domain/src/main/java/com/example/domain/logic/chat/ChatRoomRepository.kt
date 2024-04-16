package com.example.domain.logic.chat

import com.example.domain.ApiService
import com.example.domain.base.GsonUtil
import com.example.domain.common.Callback
import com.example.domain.device.IToast
import com.example.domain.socket.ILogicAction
import com.example.domain.socket.OpConst
import com.example.domain.socket.RawDataOperator
import com.example.domain.socket.msgdealer.RawDataStruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class ChatRoomRepository : IChatRoomRepository {

    override val newMsgFlow: Flow<RoomMsg>
        get() = RoomMsgManager.getManger().pushMsgFlow

    override fun clear() {

    }

    override fun sendMsg(content: String, roomId: Int, uid: Int) {
        val roomMsg = RoomMsg().apply {
            this.roomId = roomId
            this.content = content
            this.sendUid = uid
            this.msgId = "${uid}_${System.currentTimeMillis()}"
        }
        val socketManager = ApiService[ILogicAction::class.java]
        val dataOperator = ApiService[RawDataOperator::class.java]
        if (socketManager.isConnected()) {
            println("start write data")
            val outData = dataOperator.constructData(
                RawDataStruct(
                    0L, OpConst.OP_SEND_MESSAGE_ALL,
                    GsonUtil.gson.toJson(roomMsg).toByteArray(), "message"
                )
            )
            socketManager.write(
                outData.first,
                outData.second,
                object : ILogicAction.SeqCallback {

                    override fun onSuccess() {
                        RoomMsgManager.getManger()
                            .onReceiveNewMsg(roomMsg)
                    }

                    override fun onFail(code: Int, msg: String) {
                        ApiService[IToast::class.java].showToast(msg, 0)
                    }

                }
            )
        } else {
            println("write data but SocketManager is not Connected")
        }
    }


    override fun loadOldMsg(roomId: Int): Flow<List<RoomMsg>> {
        return RoomMsgManager.getManger().getMsgSByRoomId(roomId)
    }
}