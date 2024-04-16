package com.example.cleanapp.chat

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.logic.chat.RoomMsg

class RoomMsgDiffCallback(
    private val oldData: List<RoomMsg>,
    private val newData: List<RoomMsg>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    /**
     * 判断两个item是否相同
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].msgId == newData[newItemPosition].msgId
    }

    /**
     * 判断两个item是否含有相同数据
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].msgId == newData[newItemPosition].msgId
    }
}