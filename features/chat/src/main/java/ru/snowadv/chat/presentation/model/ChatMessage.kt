package ru.snowadv.chat.presentation.model

import ru.snowadv.chat.domain.model.ChatReaction
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
): DelegateItem