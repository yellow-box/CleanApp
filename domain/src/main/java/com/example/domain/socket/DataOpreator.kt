package com.example.domain.socket

import java.lang.Exception
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.atomic.AtomicLong

object DataOperator {
    const val OP_BIND_USER = 1
    //心跳包
    const val OP_HEART_BEAT = 2
    const val OP_SEND_MESSAGE_ALL = 3
    const val OP_PUSH_MESSAGE = 5
    const val OP_CREATE_ROOM = 6
    const val OP_JOIN_ROOM = 7

    // 规定最开始8个字节 表示 seq序号，接着4个字节表示 opType,后续字节表示具体内容
    private const val SEQ_LEN = 8
    private const val OP_LEN = 4
    private val curSeq: AtomicLong = AtomicLong(1)


    /**
     * @return seq,opType,contentArray
     */
    fun parseRawData(byteArray: ByteArray): Triple<Long, Int, ByteArray> {
        val byteBuffer = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN)

        val seq = byteBuffer.getLong()
        val opType = byteBuffer.getInt()
        val headLen = SEQ_LEN + OP_LEN
        if (byteArray.size < headLen) {
            throw Exception("raw array len must larger than $headLen")
        }
        val contentByteArray = ByteArray(byteArray.size - headLen)
        byteArray.copyInto(contentByteArray, startIndex = headLen)
        return Triple(seq, opType, contentByteArray)
    }

    fun constructData(opType: Int, dataContent: ByteArray): ByteArray {
        val byteBuffer = ByteBuffer.allocate(dataContent.size + OP_LEN + SEQ_LEN)
        byteBuffer.putLong(curSeq.incrementAndGet())
        byteBuffer.putInt(opType)
        byteBuffer.put(dataContent)
        return byteBuffer.array()
    }
}