package ru.snowadv.chat_presentation.chat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.snowadv.chat_presentation.chat.ui.model.ChatTopic
import ru.snowadv.chat_presentation.databinding.ItemChatTopicBinding
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate
import kotlin.math.abs

internal class TopicAdapterDelegate(
    private val onTopicClickListener: ((ChatTopic) -> Unit)? = null,
) :
    DelegationItemAdapterDelegate<ChatTopic, TopicAdapterDelegate.ChatTopicViewHolder, Nothing>() {
    internal inner class ChatTopicViewHolder(
        private val binding: ItemChatTopicBinding,
        private val colors: IntArray,
    ) :
        ViewHolder(binding.root) {

        fun initClickListeners(getCurrentList: () -> List<DelegateItem>) = with(binding) {
            chatTopicBackgroundView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(getCurrentList(), adapterPosition)?.let {
                        onTopicClickListener?.invoke(it)
                    }
                }
            }
        }

        fun bind(topic: ChatTopic) = with(binding) {
            chatTopicNameText.text = topic.name
            bindColor(topic.name)
        }
        private fun bindColor(name: String) = with(binding) {
            chatTopicBackgroundView.setColorFilter(colors[abs(name.hashCode()) % colors.size])
        }
    }

    override fun isForViewType(item: DelegateItem): Boolean {
        return item is ChatTopic
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        getCurrentList: () -> List<DelegateItem>
    ): ViewHolder {
        val binding = ItemChatTopicBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val colors = binding.root.context.resources.getIntArray(ru.snowadv.presentation.R.array.topics_colors)

        return ChatTopicViewHolder(binding, colors).also { streamViewHolder ->
            streamViewHolder.initClickListeners(getCurrentList)
        }
    }

    override fun onBindViewHolder(
        item: ChatTopic,
        holder: ChatTopicViewHolder,
        payloads: List<Nothing>
    ) {
        holder.bind(item)
    }
}