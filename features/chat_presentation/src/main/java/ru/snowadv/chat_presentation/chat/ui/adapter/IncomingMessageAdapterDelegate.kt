package ru.snowadv.chat_presentation.chat.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import io.noties.markwon.Markwon
import ru.snowadv.chat_presentation.chat.ui.model.ChatMessage
import ru.snowadv.chat_presentation.chat.ui.model.ChatMessageType
import ru.snowadv.chat_presentation.chat.ui.model.ChatReaction
import ru.snowadv.chat_presentation.chat.ui.util.AdapterUtils.getReadStatus
import ru.snowadv.chat_presentation.chat.ui.util.AdapterUtils.getReadString
import ru.snowadv.chat_presentation.chat.ui.view.IncomingMessageLayout
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate
import ru.snowadv.presentation.util.DateTimeFormatter
import java.time.LocalDateTime

internal class IncomingMessageAdapterDelegate(
    private val onLongMessageClickListener: ((ChatMessage) -> Unit)? = null,
    private val onReactionClickListener: ((reaction: ChatReaction, message: ChatMessage) -> Unit)? = null,
    private val onAddReactionClickListener: ((ChatMessage) -> Unit)? = null,
    private val timestampFormatter: DateTimeFormatter,
    private val markwon: Markwon,
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
            messageLayout.onReactionClickListener = { _, emojiCode, _ ->
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
            bindContent(message.text)
            bindIsReadAndTimestamp(message.sentAt, message.isRead)
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
                is ChatMessage.Payload.ReactionsHaveChanged -> {
                    bindReactions(payload.reactions)
                }

                is ChatMessage.Payload.ContentHasChanged -> {
                    bindContent(payload.newContent)
                }
                is ChatMessage.Payload.ReadStatusHasChanged -> {
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
            timestampText = "${timestampFormatter.format(sentAt)} ${getReadString(readStatus)}"
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

    override fun genericOnViewAttachedToWindow(
        holder: IncomingMessageViewHolder,
        getCurrentList: () -> List<DelegateItem>
    ) {
        holder.bindReactions(null)
        if (holder.adapterPosition != RecyclerView.NO_POSITION) {
            getItemAtPosition(getCurrentList(), holder.adapterPosition)?.let { message ->
                holder.bindReactions(message.reactions)
            }
        }
    }

    override fun genericOnViewDetachedFromWindow(
        holder: IncomingMessageViewHolder,
        getCurrentList: () -> List<DelegateItem>
    ) {
        holder.bindReactions(null)
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