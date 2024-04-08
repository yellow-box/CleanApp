package com.example.domain.socket

import com.example.domain.Api
import com.example.domain.socket.msgdealer.DataStruct
import com.example.domain.socket.msgdealer.RawDataStruct

/**
 * 对于网络数据与内存数据的之间映射规则 的抽象
 */
interface RawDataOperator : Api {
    fun parseRawData(byteArray: ByteArray): RawDataStruct

    fun constructData(data: RawDataStruct): Pair<ByteArray,Long>
}