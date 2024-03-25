package ru.snowadv.chat.presentation.util

import ru.snowadv.chat.domain.model.Emoji
import ru.snowadv.chat.presentation.model.ChatEmoji
import ru.snowadv.chat.domain.model.ChatMessage as DomainChatMessage
import ru.snowadv.chat.presentation.model.ChatMessage
import ru.snowadv.chat.presentation.model.ChatMessageType
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.util.toLocalDateTimeWithCurrentZone


internal fun DomainChatMessage.toUiChatMessage(currentUserId: Long): ChatMessage {
    return ChatMessage(
        id = id,
        text = content,
        sentAt = sentAt.toLocalDateTimeWithCurrentZone(),
        senderId = senderId,
        senderName = senderName,
        senderAvatarUrl = senderAvatarUrl,
        reactions = reactions,
        messageType = if (currentUserId == senderId) ChatMessageType.OUTGOING else ChatMessageType.INCOMING
    )
}

internal fun Emoji.toUiChatEmoji(): ChatEmoji {
    return ChatEmoji(name, code)
}
