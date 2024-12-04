package com.example.testapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.databinding.ItemChatBinding

class ChatList(private val chatList: List<Chat>) : RecyclerView.Adapter<ChatList.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.bind(chat)
    }

    override fun getItemCount() = chatList.size

    class ChatViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.tvSenderName.text = chat.senderName
            binding.tvLastMessage.text = chat.lastMessage
            binding.tvTime.text = chat.time
            binding.ivProfileImage.setImageResource(chat.profileImageResId)
        }
    }
}
