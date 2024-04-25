package ru.snowadv.chat.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import ru.snowadv.chat.presentation.view.IncomingMessageLayout
import ru.snowadv.chat.presentation.model.ChatReaction
import ru.snowadv.chat.presentation.model.ChatMessage
import ru.snowadv.chat.presentation.model.ChatMessageType
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate
import ru.snowadv.presentation.util.DateTimeFormatter

internal class IncomingMessageAdapterDelegate(
    private val onLongMessageClickListener: ((ChatMessage) -> Unit)? = null,
    private val onReactionClickListener: ((reaction: ChatReaction, message: ChatMessage) -> Unit)? = null,
    private val onAddReactionClickListener: ((ChatMessage) -> Unit)? = null,
    private val timestampFormatter: DateTimeFormatter
) :
    DelegationItemAdapterDelegate<ChatMessage, IncomingMessageAdapterDelegate.IncomingMessageViewHolder, ChatMessage.Payload>() {

    internal inner class IncomingMessageViewHolder(private val messageLayout: IncomingMessageLayout) :
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
                        message.reactions.firstOrNull { it.emojiCode == emojiCode }?.let {
                            onReactionClickListener?.invoke(it, message)
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
            usernameText = message.senderName
            messageText = message.text
            timestampText = timestampFormatter.format(message.sentAt)
            message.senderAvatarUrl?.let { url ->
                avatarImageView.load(url) {
                    crossfade(true)
                    placeholder(ru.snowadv.presentation.R.drawable.ic_user_avatar)
                }
            } ?: run {
                avatarImageView.setImageResource(ru.snowadv.presentation.R.drawable.ic_user_avatar)
            }
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
        return (item as? ChatMessage)?.messageType == ChatMessageType.INCOMING
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        getCurrentList: () -> List<DelegateItem>
    ): ViewHolder {
        val holder = IncomingMessageViewHolder(getNewIncomingMessageLayout(parent))
        holder.initClickListeners(getCurrentList)
        return holder
    }

    override fun onBindViewHolder(
        item: ChatMessage,
        holder: IncomingMessageViewHolder,
        payloads: List<ChatMessage.Payload>
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
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }


}