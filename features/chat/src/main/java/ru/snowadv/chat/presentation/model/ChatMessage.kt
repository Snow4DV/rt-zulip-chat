package ru.snowadv.chat.presentation.model

import ru.snowadv.chat.presentation.model.ChatReaction
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
    val messageType: ChatMessageType
) : DelegateItem {
    override fun getPayload(oldItem: DelegateItem): Any? {
        return if (oldItem is ChatMessage && checkIfOnlyReactionsChanged(oldItem)) {
            Payload.ReactionsChanged(reactions)
        } else {
            null
        }
    }

    sealed class Payload {
        class ReactionsChanged(val reactions: List<ChatReaction>) : Payload()
    }

    private fun checkIfOnlyReactionsChanged(other: ChatMessage): Boolean {
        return this.id == other.id && this.text == other.text && this.sentAt == other.sentAt
                && this.senderId == other.senderId && this.senderName == other.senderName
                && this.senderAvatarUrl == other.senderAvatarUrl && this.messageType == other.messageType
                && this.reactions != other.reactions
    }

}


