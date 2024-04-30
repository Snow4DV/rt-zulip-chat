package ru.snowadv.voiceapp.glue.util

import ru.snowadv.chat_api.domain.model.ChatEmoji
import ru.snowadv.chat_api.domain.model.ChatMessage
import ru.snowadv.chat_api.domain.model.ChatPaginatedMessages
import ru.snowadv.chat_api.domain.model.ChatReaction
import ru.snowadv.emojis_data.model.DataEmoji
import ru.snowadv.messages_data_api.model.DataMessage
import ru.snowadv.message_data.model.DataPaginatedMessages
import ru.snowadv.messages_data_api.model.DataReaction

object MessageDataMappers {
    internal fun ru.snowadv.messages_data_api.model.DataMessage.toChatMessage(): ru.snowadv.chat_api.domain.model.ChatMessage {
        return ru.snowadv.chat_api.domain.model.ChatMessage(
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

    internal fun DataEmoji.toChatEmoji(): ru.snowadv.chat_api.domain.model.ChatEmoji {
        return ChatEmoji(name = name, code = code)
    }

    internal fun ru.snowadv.messages_data_api.model.DataReaction.toChatReaction(): ru.snowadv.chat_api.domain.model.ChatReaction {
        return ru.snowadv.chat_api.domain.model.ChatReaction(
            name = emojiName,
            emojiCode = emojiCode,
            count = count,
            userReacted = userReacted
        )
    }

    internal fun DataPaginatedMessages.toChatPaginatedMessages(): ru.snowadv.chat_api.domain.model.ChatPaginatedMessages {
        return ChatPaginatedMessages(
            messages = messages.map { it.toChatMessage() },
            foundAnchor = foundAnchor,
            foundOldest = foundOldest,
            foundNewest = foundNewest,
        )
    }
}