package com.example.domain.logic.chat

import com.example.domain.ApiService
import com.example.domain.base.GsonUtil
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

    override fun clear() {
        RoomMsgManager.getManger().pushMsgListener = null
    }

    override fun sendMsg(content: String, roomId: Int, uid: Int) {
        val roomMsg = RoomMsg().apply {
            this.roomId = roomId
            this.content = content
            this.sendUid = uid
        }
        val socketManager = ApiService[ILogicAction::class.java] ?: return
        val dataOperator = ApiService[RawDataOperator::class.java] ?: return
        if (socketManager.isConnected()) {
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
                        ApiService[IToast::class.java]?.showToast(msg, 0)
                    }

                }
            )
        }

    }

    override fun loadOldMsg(roomId: Int): List<RoomMsg> {
        return RoomMsgManager.getManger().getMsgSByRoomId(roomId) ?: emptyList()
    }

}