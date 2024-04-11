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
        reactions = reactions.toChatReactions(1L), // TODO: implement logic, hardcoded at this point
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

internal fun DataEmoji.toChatEmoji(): ChatEmoji {
    return ChatEmoji(name, code)
}