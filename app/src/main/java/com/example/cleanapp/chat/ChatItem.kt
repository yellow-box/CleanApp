package com.example.cleanapp.chat

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cleanapp.R
import com.example.domain.ApiService
import com.example.domain.device.ILoginUser
import com.example.domain.logic.chat.VRoomMsg
import com.example.domain.logic.user.UserManager
import com.example.domain.memostore.INVALID_UID
import com.example.domain.memostore.InMemoDataCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatAdapter : Adapter<ChatItemVh>() {
    private val ITEM_TYPE_SYS = 1
    private val ITEM_TYPE_SELF = 2
    private val ITEM_TYPE_OTHER = 3


    private val dataS = mutableListOf<VRoomMsg>()
    private var loginUid = INVALID_UID

    init {
        CoroutineScope(Dispatchers.Main).launch {
            ApiService[ILoginUser::class.java].getUid().collect {
                loginUid = it
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<VRoomMsg>) {
        dataS.clear()
        dataS.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(msg: VRoomMsg) {
        println("add msg")
        dataS.add(msg)
        notifyItemInserted(dataS.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemVh {
        val vh = when (viewType) {
            ITEM_TYPE_SYS -> ChatItemSysVh(parent, R.layout.chat_item_sys)
            ITEM_TYPE_SELF -> ChatItemSelfVh(parent, R.layout.self_chat_item)
            ITEM_TYPE_OTHER -> ChatItemOtherVh(parent, R.layout.chat_item_other)
            else -> ChatItemSysVh(parent, R.layout.chat_item_sys)
        }
        return vh
    }

    override fun getItemViewType(position: Int): Int {
        val type = when (dataS[position].sender) {
            UserManager.SystemUid -> ITEM_TYPE_SYS
            loginUid -> ITEM_TYPE_SELF
            else -> ITEM_TYPE_OTHER
        }
        return type
    }

    override fun getItemCount(): Int {
        return dataS.size
    }

    override fun onBindViewHolder(holder: ChatItemVh, position: Int) {
        holder.update(dataS[position])
    }
}

open class ChatItemVh(itemView: View) : ViewHolder(itemView) {
    protected val contentTv = itemView.findViewById<TextView>(R.id.content)
    open fun update(msg: VRoomMsg) {
        contentTv.text = msg.content
    }
}

abstract class ChatPersonItemVh(itemView: View) : ChatItemVh(itemView) {
    protected val nameTv = itemView.findViewById<TextView>(R.id.name)

    override fun update(msg: VRoomMsg) {
        super.update(msg)
        contentTv.background = getBgDrawable()
        nameTv.text = msg.sender.toString()
    }

    abstract fun getBgDrawable(): Drawable

}

class ChatItemSysVh(parent: ViewGroup, layoutId: Int) :
    ChatItemVh(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {
}

class ChatItemSelfVh(parent: ViewGroup, layoutId: Int) :
    ChatPersonItemVh(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

    override fun getBgDrawable(): Drawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 80f
            setColor(Color.parseColor("#aaD721"))
        }
    }
}

class ChatItemOtherVh(parent: ViewGroup, layoutId: Int) :
    ChatPersonItemVh(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {
    override fun getBgDrawable(): Drawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 80f
            setColor(Color.parseColor("#ffD721"))
        }
    }
}
