package ru.snowadv.channels_presentation.stream_list.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.snowadv.channels_presentation.R
import ru.snowadv.channels_presentation.databinding.ItemTopicBinding
import ru.snowadv.channels_presentation.stream_list.ui.model.UiTopic
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate
import kotlin.math.abs

internal class TopicAdapterDelegate(
    private val onTopicClickListener: ((UiTopic) -> Unit)? = null,
) :
    DelegationItemAdapterDelegate<UiTopic, TopicAdapterDelegate.UiTopicViewHolder, UiTopic.Payload>() {
    internal inner class UiTopicViewHolder(
        private val binding: ItemTopicBinding,
        private val colors: IntArray
    ) :
        ViewHolder(binding.root) {

        fun initClickListeners(getCurrentList: () -> List<DelegateItem>) = with(binding) {
            root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(getCurrentList(), adapterPosition)?.let {
                        onTopicClickListener?.invoke(it)
                    }
                }
            }
        }

        fun bind(UiTopic: UiTopic) = with(binding) {
            topicNameText.text = UiTopic.name
            bindMsgCounter(UiTopic.unreadMessagesCount)
            bindColor(UiTopic.name)
        }

        fun handlePayload(payload: UiTopic.Payload) {
            when(payload) {
                is UiTopic.Payload.MsgCounterChanged -> bindMsgCounter(payload.newCounter)
            }
        }

        private fun bindMsgCounter(newCounter: Int) = with(binding) {
            topicMsgCounter.isVisible = newCounter > 0
            topicMsgCounter.text = root.context.getString(R.string.msg_counter, newCounter)
        }

        private fun bindColor(name: String) = with(binding) {
            topicBackgroundView.setBackgroundColor(colors[abs(name.hashCode()) % colors.size])
        }
    }

    override fun isForViewType(item: DelegateItem): Boolean {
        return item is UiTopic
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        getCurrentList: () -> List<DelegateItem>
    ): ViewHolder {
        val binding = ItemTopicBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val colors = binding.root.context.resources.getIntArray(R.array.topics_colors)

        return UiTopicViewHolder(binding, colors).also { streamViewHolder ->
            streamViewHolder.initClickListeners(getCurrentList)
        }
    }

    override fun onBindViewHolder(
        item: UiTopic,
        holder: UiTopicViewHolder,
        payloads: List<UiTopic.Payload>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            payloads.forEach(holder::handlePayload)
        }
    }
}