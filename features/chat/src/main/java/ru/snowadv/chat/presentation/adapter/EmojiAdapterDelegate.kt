package ru.snowadv.chat.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.snowadv.chat.databinding.ItemChatDateSplitterBinding
import ru.snowadv.chat.databinding.ItemEmojiBinding
import ru.snowadv.chat.domain.model.Emoji
import ru.snowadv.chat.presentation.model.ChatDate
import ru.snowadv.chat.presentation.model.ChatEmoji
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate

typealias OnEmojiItemClickListener = (ChatEmoji) -> Unit

internal class EmojiAdapterDelegate(private val listener: OnEmojiItemClickListener? = null) :
    DelegationItemAdapterDelegate<ChatEmoji, EmojiAdapterDelegate.ChatEmojiViewHolder, Nothing>() {
    internal inner class ChatEmojiViewHolder(private val binding: ItemEmojiBinding) :
        ViewHolder(binding.root) {
        fun bind(emoji: ChatEmoji) = with(binding) {
            root.text = emoji.getCodeString()
        }

        fun initClickListeners(items: List<DelegateItem>) {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(items, adapterPosition)?.let {
                        listener?.invoke(it)
                    }
                }
            }
        }
    }

    override fun isForViewType(item: DelegateItem): Boolean {
        return item is ChatEmoji
    }

    override fun onCreateViewHolder(parent: ViewGroup, items: List<DelegateItem>): ViewHolder {
        return ChatEmojiViewHolder(
            ItemEmojiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).also { it.initClickListeners(items) }
    }

    override fun onBindViewHolder(
        item: ChatEmoji,
        holder: ChatEmojiViewHolder,
        payloads: List<Nothing>
    ) {
        holder.bind(item)
    }
}