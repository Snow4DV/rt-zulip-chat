package ru.snowadv.chat.presentation.util

import ru.snowadv.chat.presentation.model.ChatEmoji
import ru.snowadv.chat.domain.model.ChatEmoji as ModelChatEmoji
import ru.snowadv.chat.domain.model.ChatReaction as DomainChatReaction
import ru.snowadv.chat.presentation.model.ChatEmoji as UiChatEmoji
import ru.snowadv.chat.domain.model.ChatMessage as DomainChatMessage
import ru.snowadv.chat.presentation.model.ChatMessage
import ru.snowadv.chat.presentation.model.ChatMessageType
import ru.snowadv.chat.presentation.model.ChatReaction
import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.event_api.model.EventMessage
import ru.snowadv.event_api.model.EventReaction
import ru.snowadv.presentation.util.toLocalDateTimeWithCurrentZone
import ru.snowadv.utils.EmojiUtils


internal fun DomainChatMessage.toUiChatMessage(): ChatMessage {
    return ChatMessage(
        id = id,
        text = content,
        sentAt = sentAt.toLocalDateTimeWithCurrentZone(),
        senderId = senderId,
        senderName = senderName,
        senderAvatarUrl = senderAvatarUrl,
        reactions = reactions.map { it.toUiChatReaction() },
        messageType = if (owner) ChatMessageType.OUTGOING else ChatMessageType.INCOMING
    )
}

internal fun DomainChatReaction.toUiChatReaction(): ChatReaction {
    return ChatReaction(
        name = name,
        emojiCode = emojiCode,
        count = count,
        userReacted = userReacted,
        emojiString = EmojiUtils.combinedHexToString(emojiCode),
    )
}

internal fun ModelChatEmoji.toUiChatEmoji(): UiChatEmoji {
    return UiChatEmoji(
        name = name,
        code = code,
        convertedEmojiString = EmojiUtils.combinedHexToString(code)
    )
}

internal fun EventReaction.toChatReaction(): ChatReaction {
    return ChatReaction(
        name = name,
        emojiCode = emojiCode,
        count = count,
        userReacted = userReacted,
        emojiString = EmojiUtils.combinedHexToString(emojiCode)
    )
}

internal fun DomainEvent.ReactionDomainEvent.toUiChatEmoji(): ChatEmoji {
    return ChatEmoji(
        name = emojiName,
        code = emojiCode,
        convertedEmojiString = EmojiUtils.combinedHexToString(emojiCode)
    )
}

internal fun EventMessage.toUiChatMessage(): ChatMessage {
    return ChatMessage(
        id = id,
        text = content,
        sentAt = sentAt.toLocalDateTimeWithCurrentZone(),
        senderId = senderId,
        senderName = senderFullName,
        senderAvatarUrl = avatarUrl,
        reactions = reactions.map { it.toChatReaction() },
        messageType = if (owner) ChatMessageType.OUTGOING else ChatMessageType.INCOMING
    )
}
