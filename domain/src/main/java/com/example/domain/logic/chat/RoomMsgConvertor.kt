package com.example.domain.logic.chat

import com.example.libannotation.ClassMapping
import com.example.libannotation.MappingConverter

@MappingConverter
abstract class RoomMsgConvertor: ClassMapping<RoomMsg, VRoomMsg>() {
}