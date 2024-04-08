package com.example.cleanapp.chat

import com.example.domain.logic.chat.VRoomMsg

/**
 * 对 聊天消息 显示逻辑的抽象
 */
interface IChatAction {
    fun showHistory(msgS: List<VRoomMsg>)
    fun showMyselfNewMsg(msg: VRoomMsg)
    fun showNewOtherNewMsg(msg: VRoomMsg)

    fun showErrorMsg(msg: String)
}