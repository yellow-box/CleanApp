package com.example.domain.logic.chat
import com.example.libannotation.ClassMapping

/**
 * 负责网络中的RoomMsg 数据与 view中的VRoomMsg之间的相互映射
 */
object ChatModelMapping {
    private val modelMapping = ClassMapping.Builder(RoomMsgConvertor::class.java).build()
    fun toViewModel(roomMsg: RoomMsg) = modelMapping.convert(roomMsg)


    fun toDataModel(msg: VRoomMsg) = modelMapping.revers(msg)


    fun toViewModels(msgS: List<RoomMsg>): List<VRoomMsg> {
        return msgS.map {
            modelMapping.convert(it)
        }
    }
}