package ru.snowadv.chat_presentation.chat.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.noties.markwon.Markwon
import ru.snowadv.chat_presentation.chat.ui.model.ChatMessage
import ru.snowadv.chat_presentation.chat.ui.model.ChatMessageType
import ru.snowadv.chat_presentation.chat.ui.model.ChatReaction
import ru.snowadv.chat_presentation.chat.ui.util.AdapterUtils
import ru.snowadv.chat_presentation.chat.ui.view.OutgoingMessageLayout
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate
import ru.snowadv.presentation.util.DateTimeFormatter
import java.time.LocalDateTime

internal class OutgoingMessageAdapterDelegate(
    private val onAddReactionClickListener: ((ChatMessage) -> Unit)? = null,
    private val onLongMessageClickListener: ((ChatMessage) -> Unit)? = null,
    private val onReactionClickListener: ((reaction: ChatReaction, message: ChatMessage) -> Unit)? = null,
    private val timestampFormatter: DateTimeFormatter,
    private val markwon: Markwon,
) :
    DelegationItemAdapterDelegate<ChatMessage, OutgoingMessageAdapterDelegate.OutgoingMessageViewHolder, ChatMessage.Payload>() {
    internal inner class OutgoingMessageViewHolder(val messageLayout: OutgoingMessageLayout) :
        ViewHolder(messageLayout.rootView) {

        fun initClickListeners(getCurrentList: () -> List<DelegateItem>) {
            messageLayout.onMessageLongClickListener = {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(getCurrentList(), adapterPosition)?.let {
                        onLongMessageClickListener?.invoke(it)
                    }
                }
            }
            messageLayout.onReactionClickListener = { _, emojiCode, _ ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(getCurrentList(), adapterPosition)?.let { message ->
                        message.reactions.firstOrNull { it.emojiCode == emojiCode }
                            ?.let { reaction ->
                                onReactionClickListener?.invoke(reaction, message)
                            }
                    }
                }
            }
            messageLayout.onAddReactionClickListener = {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(getCurrentList(), adapterPosition)?.let { message ->
                        onAddReactionClickListener?.invoke(message)
                    }
                }
            }
        }

        fun bind(message: ChatMessage) = with(messageLayout) {
            bindContent(message.text)
            bindIsReadAndTimestamp(message.sentAt, message.isRead)
            bindReactions(message.reactions)
        }

        fun handlePayload(payload: ChatMessage.Payload) {
            when (payload) {
                is ChatMessage.Payload.ReactionsHaveChanged -> {
                    bindReactions(payload.reactions)
                }

                is ChatMessage.Payload.ContentHasChanged -> {
                    bindContent(payload.newContent)
                }
                is ChatMessage.Payload.ReadStatusOrTimestampHaveChanged -> {
                    bindIsReadAndTimestamp(payload.sentAt, payload.newIsRead)
                }
            }
        }

        fun bindReactions(reactions: List<ChatReaction>?) = with(messageLayout) {
            updateReactionsWithAsyncDiffUtil(reactions)
        }

        fun bindContent(content: String) = with(messageLayout) {
            setMarkdown(content, markwon)
        }

        fun bindIsReadAndTimestamp(sentAt: LocalDateTime, readStatus: Boolean) = with(messageLayout) {
            timestampText = "${timestampFormatter.format(sentAt)} ${AdapterUtils.getReadString(readStatus)}"
        }
    }

    override fun isForViewType(item: DelegateItem): Boolean {
        return (item as? ChatMessage)?.messageType == ChatMessageType.OUTGOING
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        getCurrentList: () -> List<DelegateItem>,
    ): ViewHolder {
        val holder = OutgoingMessageViewHolder(getNewOutgoingMessageLayout(parent))
        holder.initClickListeners(getCurrentList)
        return holder
    }

    override fun onBindViewHolder(
        item: ChatMessage,
        holder: OutgoingMessageViewHolder,
        payloads: List<ChatMessage.Payload>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else payloads.forEach {
            holder.handlePayload(it)
        }
    }

    /*
       This is important because there's an edgecase when async differ is trying to dispatch updates
       after view is detached.
        */
    override fun genericOnViewAttachedToWindow(
        holder: OutgoingMessageViewHolder,
        getCurrentList: () -> List<DelegateItem>
    ) {
        holder.bindReactions(null)
        if (holder.adapterPosition != RecyclerView.NO_POSITION) {
            getItemAtPosition(getCurrentList(), holder.adapterPosition)?.let { message ->
                holder.bindReactions(message.reactions)
                // This is required for markwon's coil plugin - otherwise fetching would be cancelled
                holder.bindContent(message.text)
            }
        }
    }

    override fun genericOnViewDetachedFromWindow(
        holder: OutgoingMessageViewHolder,
        getCurrentList: () -> List<DelegateItem>
    ) {
        holder.bindReactions(null)
    }

    private fun getNewOutgoingMessageLayout(parent: ViewGroup): OutgoingMessageLayout {
        return OutgoingMessageLayout(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            this.maxWidthOfParent = 0.9f
        }
    }
}