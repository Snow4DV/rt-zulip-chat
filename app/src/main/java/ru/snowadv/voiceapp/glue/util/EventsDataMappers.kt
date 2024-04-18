package ru.snowadv.voiceapp.glue.util

import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.chat.domain.model.ChatReaction
import ru.snowadv.events_data.model.DataEvent
import ru.snowadv.events_data.model.DataMessage
import ru.snowadv.events_data.model.DataReaction

fun DataMessage.toChatMessage(): ChatMessage {
    return ChatMessage(
        id = id,
        content = content,
        sentAt = sentAt,
        senderId = senderId,
        senderName = senderName,
        senderAvatarUrl = senderAvatarUrl,
        reactions = reactions.toChatReactions(1L), // TODO: implement auth repo. hardcoded at this point
    )
}

fun List<DataReaction>.toChatReactions(currentUserId: Long): List<ChatReaction> {
    return buildList {
        this@toChatReactions.groupBy { it.emojiName }.asSequence().map { it.value }.forEach {
            add(
                ChatReaction(
                    name = it.first().emojiName,
                    emojiCode = it.first().emojiCode,
                    count = it.size,
                    userReacted = it.any { reaction -> reaction.userId == currentUserId },
                )
            )
        }
    }
}