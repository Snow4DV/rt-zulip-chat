package ru.snowadv.voiceapp.glue.util

import ru.snowadv.chat.domain.model.ChatEmoji
import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.chat.domain.model.ChatPaginatedMessages
import ru.snowadv.chat.domain.model.ChatReaction
import ru.snowadv.emojis_data.model.DataEmoji
import ru.snowadv.message_data.model.DataMessage
import ru.snowadv.message_data.model.DataPaginatedMessages
import ru.snowadv.message_data.model.DataReaction

internal fun DataMessage.toChatMessage(): ChatMessage {
    return ChatMessage(
        id = id,
        content = content,
        sentAt = sentAt,
        senderId = senderId,
        senderName = senderName,
        senderAvatarUrl = senderAvatarUrl,
        reactions = reactions.map { it.toChatReaction() },
        owner = owner,
    )
}

internal fun DataEmoji.toChatEmoji(): ChatEmoji {
    return ChatEmoji(name = name, code = code)
}

internal fun DataReaction.toChatReaction(): ChatReaction {
    return ChatReaction(
        name = emojiName,
        emojiCode = emojiCode,
        count = count,
        userReacted = userReacted
    )
}

internal fun DataPaginatedMessages.toChatPaginatedMessages(): ChatPaginatedMessages {
    return ChatPaginatedMessages(
        messages = messages.map { it.toChatMessage() },
        foundAnchor = foundAnchor,
        foundOldest = foundOldest,
        foundNewest = foundNewest,
    )
}