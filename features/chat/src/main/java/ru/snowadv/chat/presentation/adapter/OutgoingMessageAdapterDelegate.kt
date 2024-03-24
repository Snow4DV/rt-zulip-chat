package ru.snowadv.chat.presentation.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.snowadv.chat.R
import ru.snowadv.chat.domain.model.emojiMap
import ru.snowadv.chat.presentation.view.OutgoingMessageLayout
import ru.snowadv.chat.domain.model.ChatReaction
import ru.snowadv.chat.presentation.model.ChatMessage
import ru.snowadv.chat.presentation.model.ChatMessageType
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate
import ru.snowadv.presentation.util.DateTimeFormatter

internal class OutgoingMessageAdapterDelegate(
    private val onAddReactionClick: ((ChatMessage) -> Unit)? = null,
    private val onLongMessageClick: ((ChatMessage) -> Unit)? = null,
    private val onReactionClick: ((reaction: ChatReaction, message: ChatMessage) -> Unit)? = null,
    private val timestampFormatter: DateTimeFormatter
) :
    DelegationItemAdapterDelegate<ChatMessage, OutgoingMessageAdapterDelegate.OutgoingMessageViewHolder, OutgoingMessageAdapterDelegate.OutgoingMessagePayload>() {
    internal inner class OutgoingMessageViewHolder(val messageLayout: OutgoingMessageLayout) :
        ViewHolder(messageLayout.rootView) {

        fun initClickListeners(holder: ViewHolder, items: List<DelegateItem>) {
            messageLayout.onMessageLongClickListener = {
                if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(items, holder.adapterPosition)?.let {
                        onLongMessageClick?.invoke(it)
                    }
                }
            }
            messageLayout.onReactionClickListener = { count, emojiCode, userReacted ->
                if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(items, holder.adapterPosition)?.let { message ->
                        onReactionClick?.invoke(
                            ChatReaction(
                                emojiMap[emojiCode] ?: error("No such emoji with code $emojiCode"),
                                count,
                                userReacted
                            ),
                            message
                        )
                    }
                }
            }
        }

        fun bind(message: ChatMessage) = with(messageLayout) {
            messageText = message.text
            timestampText = timestampFormatter.format(message.sentAt)
            bindReactions(message.reactions)
        }

        fun handlePayload(payload: OutgoingMessagePayload) {
            when (payload) {
                is OutgoingMessagePayload.ReactionsChanged -> {
                    bindReactions(payload.reactions)
                }
            }
        }

        private fun bindReactions(reactions: List<ChatReaction>) = with(messageLayout) {
            replaceReactions(reactions)
        }
    }

    override fun isForViewType(item: DelegateItem): Boolean {
        return (item as? ChatMessage)?.messageType == ChatMessageType.OUTGOING
    }

    override fun onCreateViewHolder(parent: ViewGroup, items: List<DelegateItem>): ViewHolder {
        val holder = OutgoingMessageViewHolder(getNewOutgoingMessageLayout(parent))
        holder.initClickListeners(holder, items)
        return holder
    }

    override fun onBindViewHolder(
        item: ChatMessage,
        holder: OutgoingMessageViewHolder,
        payloads: List<OutgoingMessagePayload>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else payloads.forEach {
            holder.handlePayload(it)
        }
    }

    private fun getNewOutgoingMessageLayout(parent: ViewGroup): OutgoingMessageLayout {
        return OutgoingMessageLayout(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(getMessageLeftPaddingInPx(parent.context, parent.measuredWidth), 0, 0, 0)
        }
    }

    private fun getMessageLeftPaddingInPx(context: Context, parentWidth: Int): Int {
        return (ResourcesCompat.getFloat(
            context.resources,
            R.dimen.message_side_padding
        ) * parentWidth).toInt()
    }

    sealed class OutgoingMessagePayload {
        class ReactionsChanged(val reactions: List<ChatReaction>) : OutgoingMessagePayload()
    }


}