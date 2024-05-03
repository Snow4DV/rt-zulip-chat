package ru.snowadv.chat_impl.data.util

import ru.snowadv.chat_impl.domain.model.ChatEmoji
import ru.snowadv.chat_impl.domain.model.ChatMessage
import ru.snowadv.chat_impl.domain.model.ChatPaginatedMessages
import ru.snowadv.chat_impl.domain.model.ChatReaction
import ru.snowadv.emojis_data_api.model.model.DataEmoji
import ru.snowadv.messages_data_api.model.DataMessage
import ru.snowadv.messages_data_api.model.DataPaginatedMessages
import ru.snowadv.messages_data_api.model.DataReaction
internal object MessageDataMappers {
    fun DataMessage.toChatMessage(): ChatMessage {
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

    fun DataEmoji.toChatEmoji(): ChatEmoji {
        return ChatEmoji(name = name, code = code)
    }

    private fun DataReaction.toChatReaction(): ChatReaction {
        return ChatReaction(
            name = emojiName,
            emojiCode = emojiCode,
            count = count,
            userReacted = userReacted
        )
    }

    fun DataPaginatedMessages.toChatPaginatedMessages(): ChatPaginatedMessages {
        return ChatPaginatedMessages(
            messages = messages.map { it.toChatMessage() },
            foundAnchor = foundAnchor,
            foundOldest = foundOldest,
            foundNewest = foundNewest,
        )
    }
}