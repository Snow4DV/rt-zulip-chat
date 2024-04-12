package ru.snowadv.voiceapp.glue.util

import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.chat.domain.model.ChatReaction
import ru.snowadv.events_data.model.DataEvent
import ru.snowadv.events_data.model.DataMessage
import ru.snowadv.events_data.model.DataReaction

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

internal fun DataReaction.toChatReaction(): ChatReaction {
    return ChatReaction(emojiName, emojiCode, count, userReacted)
}