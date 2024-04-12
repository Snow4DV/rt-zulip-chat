package ru.snowadv.voiceapp.glue.util

import ru.snowadv.chat.domain.model.ChatEmoji
import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.chat.domain.model.ChatReaction
import ru.snowadv.emojis_data.model.DataEmoji
import ru.snowadv.message_data.model.DataMessage
import ru.snowadv.message_data.model.DataReaction

internal fun DataMessage.toChatMessage(): ChatMessage {
    return ChatMessage(
        id = id,
        content = content,
        sentAt = sentAt,
        senderId = senderId,
        senderName = senderName,
        senderAvatarUrl = senderAvatarUrl,
        reactions = reactions.map { it.toChatReaction() }
    )
}

internal fun DataEmoji.toChatEmoji(): ChatEmoji {
    return ChatEmoji(name, code)
}

internal fun DataReaction.toChatReaction(): ChatReaction {
    return ChatReaction(emojiName, emojiCode, count, userReacted)
}