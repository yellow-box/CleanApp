package com.example.domain.logic.chat.usecase

import com.example.domain.common.IUseCase
import com.example.domain.logic.chat.IChatRoomRepository
import com.example.domain.logic.chat.RoomMsg
import kotlinx.coroutines.flow.Flow


/**
 * 监听搜到新的push消息
 */
class ReceiveNewMsgUseCase(private val chatRoomRepository: IChatRoomRepository) :
    IUseCase<Flow<RoomMsg>, Int>() {
    override suspend fun run(param: Int): Flow<RoomMsg> {
        return chatRoomRepository.newMsgFlow
    }
}