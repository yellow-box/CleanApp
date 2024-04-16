package com.example.domain.logic.chat.usecase

import com.example.domain.common.IUseCase
import com.example.domain.logic.chat.IChatRoomRepository
import com.example.domain.logic.chat.RoomMsg


/**
 * 发送消息
 */
class SendMsgUseCase(private val chatRoomRepository: IChatRoomRepository) :
    IUseCase<Nothing, RoomMsg>() {
    override suspend fun run(param: RoomMsg): Nothing? {
        chatRoomRepository.sendMsg(param.content, param.roomId, param.sendUid)
        return null
    }
}