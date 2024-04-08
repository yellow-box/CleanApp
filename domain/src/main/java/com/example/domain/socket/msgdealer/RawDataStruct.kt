package com.example.domain.socket.msgdealer

/**
 * @param seq 某次消息
 * @param opType 消息类型
 * @param dataContent 实际的数据内内容
 */
class RawDataStruct(val seq:Long,val opType:Int,val dataContent: ByteArray,val source:String): DataStruct() {

}