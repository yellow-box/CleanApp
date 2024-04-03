package com.example.domain.logic.chat

import com.example.domain.ApiService
import com.example.domain.base.GsonUtil
import com.example.domain.common.Callback
import com.example.domain.device.IToast
import com.example.domain.socket.ILogicAction
import com.example.domain.socket.OpConst
import com.example.domain.socket.RawDataOperator
import com.example.domain.socket.msgdealer.MainRouter
import com.example.domain.socket.msgdealer.RawDataStruct


class ChatRoomRepository : IChatRoomRepository {

    override fun registerNewMsgListener(l: IChatRoomRepository.NewMsgListener) {
        RoomMsgManager.getManger().pushMsgListener = object : PushMsgListener {
            override fun onPushMsg(roomMsg: RoomMsg) {
                l.onReceiveNewMsg(roomMsg)
            }
        }
    }

    override fun loadOldMsg(roomId: Int, loadCallback: Callback<List<RoomMsg>>) {
        RoomMsgManager.getManger().getMsgSByRoomId(roomId,object : Callback<List<RoomMsg>>{
            override fun call(data: List<RoomMsg>) {
                loadCallback.call(data)
            }
        })
    }

    override fun clear() {
        RoomMsgManager.getManger().pushMsgListener = null
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

}