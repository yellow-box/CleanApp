package com.example.cleanapp.chat

import com.example.domain.logic.chat.VRoomMsg

interface IChatAction {
    fun showHistory(msgS: List<VRoomMsg>)
    fun showMyselfNewMsg(msg: VRoomMsg)
    fun showNewOtherNewMsg(msg: VRoomMsg)

    fun showErrorMsg(msg: String)
}