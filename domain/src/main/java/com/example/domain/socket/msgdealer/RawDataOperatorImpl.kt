package com.example.domain.socket.msgdealer

import com.example.domain.base.printlnCallStack
import com.example.domain.socket.RawDataOperator
import java.lang.Exception
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.atomic.AtomicLong

class RawDataOperatorImpl : RawDataOperator {

    // 规定最开始8个字节 表示 seq序号，接着4个字节表示 opType,后续字节表示具体内容
    companion object {
        private const val SEQ_LEN = 8
        private const val OP_LEN = 4
    }

    private val curSeq: AtomicLong = AtomicLong(1)
    override fun parseRawData(byteArray: ByteArray): RawDataStruct {
        val headLen = SEQ_LEN + OP_LEN
        if (byteArray.size < headLen) {
            throw Exception("raw array len must larger than $headLen")
        }
        val byteBuffer = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN)

        val seq = byteBuffer.getLong()
        val opType = byteBuffer.getInt()
        val contentByteArray = ByteArray(byteArray.size - headLen)
        byteArray.copyInto(contentByteArray, startIndex = headLen)
        return RawDataStruct(seq, opType, contentByteArray, "")
    }


    /**
     * @return rawData, Seq
     */
    override fun constructData(data: RawDataStruct): Pair<ByteArray, Long> {
        val byteBuffer = ByteBuffer.allocate(data.dataContent.size + OP_LEN + SEQ_LEN)
            .order(ByteOrder.LITTLE_ENDIAN)
        val seq = curSeq.incrementAndGet()
        byteBuffer.putLong(seq)
        byteBuffer.putInt(data.opType)
        byteBuffer.put(data.dataContent)
        return Pair(byteBuffer.array(), seq)
    }
}