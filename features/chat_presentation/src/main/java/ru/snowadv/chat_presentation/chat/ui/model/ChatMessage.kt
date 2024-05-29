package ru.snowadv.chat_presentation.chat.ui.model

import ru.snowadv.presentation.adapter.DelegateItem
import java.time.LocalDateTime

internal data class ChatMessage(
    override val id: Long,
    val text: String,
    val sentAt: LocalDateTime,
    val senderId: Long,
    val senderName: String,
    val senderAvatarUrl: String?,
    val reactions: List<ChatReaction>,
    val messageType: ChatMessageType,
    val topic: String,
    val isRead: Boolean,
) : DelegateItem {
    override fun getPayload(oldItem: DelegateItem): Any? {
        if (oldItem !is ChatMessage) return null

        return when {
            checkIfOnlyReactionsHaveChanged(oldItem) ->  Payload.ReactionsHaveChanged(reactions)
            checkIfReadStatusOrTimestampHaveChanged(oldItem) -> Payload.ReadStatusOrTimestampHaveChanged(isRead, sentAt)
            checkIfOnlyContentHasChanged(oldItem) -> Payload.ContentHasChanged(text)
            else -> null
        }

    }

    sealed interface Payload {
        class ReactionsHaveChanged(val reactions: List<ChatReaction>) : Payload
        class ContentHasChanged(val newContent: String) : Payload
        class ReadStatusOrTimestampHaveChanged(val newIsRead: Boolean, val sentAt: LocalDateTime) : Payload
    }

    private fun checkIfOnlyReactionsHaveChanged(other: ChatMessage): Boolean {
        return this.id == other.id && this.text == other.text && this.sentAt == other.sentAt
                && this.senderId == other.senderId && this.senderName == other.senderName
                && this.senderAvatarUrl == other.senderAvatarUrl && this.messageType == other.messageType
                && this.reactions != other.reactions && this.topic == other.topic && this.isRead == other.isRead
    }

    private fun checkIfReadStatusOrTimestampHaveChanged(other: ChatMessage): Boolean {
        return this.id == other.id && this.text == other.text && this.senderId == other.senderId
                && this.senderName == other.senderName && this.senderAvatarUrl == other.senderAvatarUrl
                && this.messageType == other.messageType && this.reactions == other.reactions
                && this.topic == other.topic && (this.isRead != other.isRead || this.sentAt != other.sentAt)
    }

    private fun checkIfOnlyContentHasChanged(other: ChatMessage): Boolean {
        return this.id == other.id && this.text != other.text && this.sentAt == other.sentAt
                && this.senderId == other.senderId && this.senderName == other.senderName
                && this.senderAvatarUrl == other.senderAvatarUrl && this.messageType == other.messageType
                && this.reactions == other.reactions && this.topic == other.topic && this.isRead == other.isRead
    }

}


