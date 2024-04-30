package ru.snowadv.channels_impl.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.snowadv.channels_impl.R
import ru.snowadv.channels_impl.databinding.ItemTopicBinding
import ru.snowadv.channels_impl.presentation.model.Topic
import ru.snowadv.channels_impl.presentation.model.TopicUnreadMessages
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate
import kotlin.math.abs

internal class TopicAdapterDelegate(
    private val onTopicClickListener: ((Topic) -> Unit)? = null,
) :
    DelegationItemAdapterDelegate<Topic, TopicAdapterDelegate.TopicViewHolder, Topic.Payload>() {
    internal inner class TopicViewHolder(
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

        fun bind(topic: Topic) = with(binding) {
            topicNameText.text = topic.name
            bindMsgCounter(topic.unreadMessagesCount)
            bindColor(topic.name)
        }

        fun handlePayload(payload: Topic.Payload) {
            when(payload) {
                is Topic.Payload.MsgCounterChanged -> bindMsgCounter(payload.newCounter)
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
        return item is Topic
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

        return TopicViewHolder(binding, colors).also { streamViewHolder ->
            streamViewHolder.initClickListeners(getCurrentList)
        }
    }

    override fun onBindViewHolder(
        item: Topic,
        holder: TopicViewHolder,
        payloads: List<Topic.Payload>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            payloads.forEach(holder::handlePayload)
        }
    }
}