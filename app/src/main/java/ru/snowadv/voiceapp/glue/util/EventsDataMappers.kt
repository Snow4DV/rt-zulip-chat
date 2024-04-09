package ru.snowadv.voiceapp.glue.util

import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.chat.domain.model.ChatReaction
import ru.snowadv.events_data.model.DataEvent
import ru.snowadv.events_data.model.DataMessage
import ru.snowadv.events_data.model.DataReaction

fun DataMessage.toChatMessage(): ChatMessage {
    return ChatMessage(id, content, sentAt, senderId, senderName, senderAvatarUrl, reactions.map { it.toChatReaction() })
}

fun DataReaction.toChatReaction(): ChatReaction {
    return ChatReaction(userId, emojiName, emojiCode, 0, false)
}