package ru.snowadv.channels_presentation.stream_list.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.snowadv.channels_presentation.databinding.ItemStreamBinding
import ru.snowadv.channels_presentation.stream_list.ui.model.UiStream
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate

internal class StreamAdapterDelegate(
    private val onExpandStreamClickListener: ((UiStream) -> Unit)? = null,
    private val onOpenStreamClickListener: ((UiStream) -> Unit)? = null,
    private val onChangeStreamSubscriptionStatusClickListener: ((UiStream) -> Unit)? = null,
) :
    DelegationItemAdapterDelegate<UiStream, StreamAdapterDelegate.StreamViewHolder, UiStream.Payload>() {
    internal inner class StreamViewHolder(private val binding: ItemStreamBinding) :
        ViewHolder(binding.root) {
        fun initClickListeners(getCurrentList: () -> List<DelegateItem>) = with(binding) {
            root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(getCurrentList(), adapterPosition)?.let {
                        onOpenStreamClickListener?.invoke(it)
                    }
                }
            }
            expandStreamButton.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(getCurrentList(), adapterPosition)?.let {
                        onExpandStreamClickListener?.invoke(it)
                    }
                }
            }
            subscribeStreamButton.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(getCurrentList(), adapterPosition)?.let {
                        onChangeStreamSubscriptionStatusClickListener?.invoke(it)
                    }
                }
            }
        }

        fun bind(stream: UiStream) = with(binding) {
            bindExpanded(stream.expanded)
            bindSubscribeStatus(stream.subscribeStatus)
            bindUnreadMessagesCount(stream.unreadMessagesCount)
            messagesCountText.setTextColor(Color.parseColor(stream.color))
            streamNameText.text = root.context.getString(ru.snowadv.presentation.R.string.stream_title, stream.name)
        }

        fun handlePayload(payload: UiStream.Payload) {
            when (payload) {
                is UiStream.Payload.ExpandedChanged -> bindExpanded(payload.expanded)
                is UiStream.Payload.SubscribedStatusChanged -> bindSubscribeStatus(payload.newStatus)
                is UiStream.Payload.UnreadMessagesCountChanged -> bindUnreadMessagesCount(payload.newCount)
            }
        }

        private fun bindExpanded(expanded: Boolean) = with(binding) {
            expandStreamButton.isSelected = expanded
            streamSeparator.isVisible = !expanded
        }

        private fun bindSubscribeStatus(status: UiStream.SubscribeStatus) = with(binding) {
            subscribeStreamButton.isSelected = status.subscribed
            subscribeStreamButton.isVisible = !status.loading
            subscribeLoadingProgressBar.isVisible = status.loading
        }

        private fun bindUnreadMessagesCount(newCount: Int) = with(binding) {
            messagesCountText.text = newCount.toString()
            messagesCountText.isVisible = newCount > 0
        }
    }

    override fun isForViewType(item: DelegateItem): Boolean {
        return item is UiStream
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        getCurrentList: () -> List<DelegateItem>
    ): ViewHolder {
        return StreamViewHolder(
            ItemStreamBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).also { streamViewHolder ->
            streamViewHolder.initClickListeners(getCurrentList)
        }
    }

    override fun onBindViewHolder(
        item: UiStream,
        holder: StreamViewHolder,
        payloads: List<UiStream.Payload>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            payloads.forEach(holder::handlePayload)
        }
    }
}