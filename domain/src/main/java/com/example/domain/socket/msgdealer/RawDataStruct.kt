package com.example.domain.socket.msgdealer

class RawDataStruct(val seq:Long,val opType:Int,val dataContent: ByteArray,val source:String): DataStruct() {

}