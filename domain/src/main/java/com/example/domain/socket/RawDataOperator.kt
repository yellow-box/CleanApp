package com.example.domain.socket

import com.example.domain.Api
import com.example.domain.socket.msgdealer.DataStruct
import com.example.domain.socket.msgdealer.RawDataStruct

interface RawDataOperator : Api {
    fun parseRawData(byteArray: ByteArray): RawDataStruct

    fun constructData(data: RawDataStruct): Pair<ByteArray,Long>
}