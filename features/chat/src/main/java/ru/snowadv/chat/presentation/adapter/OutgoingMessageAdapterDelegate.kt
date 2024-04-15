package ru.snowadv.chat.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.snowadv.chat.presentation.view.OutgoingMessageLayout
import ru.snowadv.chat.presentation.model.ChatReaction
import ru.snowadv.chat.presentation.model.ChatMessage
import ru.snowadv.chat.presentation.model.ChatMessageType
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate
import ru.snowadv.presentation.util.DateTimeFormatter

internal class OutgoingMessageAdapterDelegate(
    private val onAddReactionClickListener: ((ChatMessage) -> Unit)? = null,
    private val onLongMessageClickListener: ((ChatMessage) -> Unit)? = null,
    private val onReactionClickListener: ((reaction: ChatReaction, message: ChatMessage) -> Unit)? = null,
    private val timestampFormatter: DateTimeFormatter
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
            messageLayout.onReactionClickListener = { count, emojiCode, userReacted ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(getCurrentList(), adapterPosition)?.let { message ->
                        message.reactions.firstOrNull { it.emojiCode == emojiCode }?.let { reaction ->
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
            messageText = message.text
            timestampText = timestampFormatter.format(message.sentAt)
            bindReactions(message.reactions)
        }

        fun handlePayload(payload: ChatMessage.Payload) {
            when (payload) {
                is ChatMessage.Payload.ReactionsChanged -> {
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        getCurrentList: () -> List<DelegateItem>
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