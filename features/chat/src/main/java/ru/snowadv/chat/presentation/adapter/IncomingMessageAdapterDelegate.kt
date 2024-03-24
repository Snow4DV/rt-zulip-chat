package ru.snowadv.chat.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import ru.snowadv.chat.domain.model.emojiMap
import ru.snowadv.chat.presentation.view.IncomingMessageLayout
import ru.snowadv.chat.domain.model.ChatReaction
import ru.snowadv.chat.presentation.model.ChatMessage
import ru.snowadv.chat.presentation.model.ChatMessageType
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate
import ru.snowadv.presentation.util.DateTimeFormatter

internal class IncomingMessageAdapterDelegate(
    private val onLongMessageClick: ((ChatMessage) -> Unit)? = null,
    private val onReactionClick: ((reaction: ChatReaction, message: ChatMessage) -> Unit)? = null,
    private val onAddReactionClick: ((ChatMessage) -> Unit)? = null,
    private val timestampFormatter: DateTimeFormatter
) :
    DelegationItemAdapterDelegate<ChatMessage, IncomingMessageAdapterDelegate.IncomingMessageViewHolder, IncomingMessageAdapterDelegate.IncomingMessagePayload>() {

    internal inner class IncomingMessageViewHolder(val messageLayout: IncomingMessageLayout) :
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
            usernameText = message.senderName
            messageText = message.text
            timestampText = timestampFormatter.format(message.sentAt)
            avatarImageView.load(message.senderAvatarUrl) {
                crossfade(true)
                placeholder(ru.snowadv.presentation.R.drawable.ic_user_avatar)
            }
            bindReactions(message.reactions)
        }

        fun handlePayload(payload: IncomingMessagePayload) {
            when (payload) {
                is IncomingMessagePayload.ReactionsChanged -> {
                    bindReactions(payload.reactions)
                }
            }
        }

        private fun bindReactions(reactions: List<ChatReaction>) = with(messageLayout) {
            replaceReactions(reactions)
        }
    }

    override fun isForViewType(item: DelegateItem): Boolean {
        return (item as? ChatMessage)?.messageType == ChatMessageType.INCOMING
    }

    override fun onCreateViewHolder(parent: ViewGroup, items: List<DelegateItem>): ViewHolder {
        val holder = IncomingMessageViewHolder(getNewIncomingMessageLayout(parent))
        holder.initClickListeners(holder, items)
        return holder
    }

    override fun onBindViewHolder(
        item: ChatMessage,
        holder: IncomingMessageViewHolder,
        payloads: List<IncomingMessagePayload>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else payloads.forEach {
            holder.handlePayload(it)
        }
    }

    private fun getNewIncomingMessageLayout(parent: ViewGroup): IncomingMessageLayout {
        return IncomingMessageLayout(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    sealed class IncomingMessagePayload {
        class ReactionsChanged(val reactions: List<ChatReaction>) : IncomingMessagePayload()
    }


}