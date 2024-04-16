package com.example.domain.logic.chat.usecase

import com.example.domain.common.IUseCase
import com.example.domain.logic.chat.IChatRoomRepository
import com.example.domain.logic.chat.RoomMsg
import kotlinx.coroutines.flow.Flow

/**
 * 加载房间原有的消息
 */
class LoadRoomMsgSUseCase(private val chatRoomRepository: IChatRoomRepository):
    IUseCase<Flow<List<RoomMsg>>, Int>() {
    override suspend fun run(param: Int): Flow<List<RoomMsg>> {
        return chatRoomRepository.loadOldMsg(param)
    }
}