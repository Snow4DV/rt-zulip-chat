package ru.snowadv.auth_data.util

import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.chat_domain_api.model.ChatPaginatedMessages
import ru.snowadv.chat_domain_api.model.ChatReaction
import ru.snowadv.database.entity.MessageEntity
import ru.snowadv.database.entity.ReactionEntity
import ru.snowadv.network.model.MessageResponseDto
import ru.snowadv.network.model.MessagesResponseDto
import ru.snowadv.network.model.ReactionResponseDto
import ru.snowadv.utils.DateUtils

internal object MessagesMapper {
    private const val UNICODE_REACTION_TYPE = "unicode_emoji"

    private fun MessageResponseDto.toChatMessage(currentUserId: Long): ChatMessage {
        return ChatMessage(
            id = id,
            content = content,
            sentAt = DateUtils.epochSecondsToZonedDateTime(timestamp),
            senderId = senderId,
            senderName = senderFullName,
            senderAvatarUrl = avatarUrl,
            reactions = reactions.fromDtotoChatReactions(currentUserId),
            owner = currentUserId == senderId,
            topic = subject,
        )
    }

    fun MessagesResponseDto.toChatPaginatedMessages(
        currentUserId: Long,
        anchorMessageId: Long?,
        includeAnchorMessage: Boolean
    ): ChatPaginatedMessages {
        return ChatPaginatedMessages(
            messages = messages.asSequence()
                .filter {
                    !(it.id == anchorMessageId && !includeAnchorMessage
                            || it.id == messages.lastOrNull()?.id && !includeAnchorMessage && anchorMessageId == null)
                }
                .map { it.toChatMessage(currentUserId) }.toList(),
            foundAnchor = foundAnchor,
            foundOldest = foundOldest,
            foundNewest = foundNewest,
        )
    }

    private fun MessageResponseDto.toEntityMessage(streamName: String): MessageEntity {
        return MessageEntity(
            id = id,
            content = content,
            senderId = senderId,
            reactions = reactions.toEntityReactions(),
            timestamp = timestamp,
            senderFullName = senderFullName,
            avatarUrl = avatarUrl,
            type = type,
            subject = subject,
            streamName = streamName,
            streamId = streamId,
            flags = flags,
        )
    }

    fun MessagesResponseDto.toEntityMessages(streamName: String): List<MessageEntity> {
        return messages.map { it.toEntityMessage(streamName) }
    }


    private fun MessageEntity.toChatMessage(currentUserId: Long): ChatMessage {
        return ChatMessage(
            id = id,
            content = content,
            sentAt = DateUtils.epochSecondsToZonedDateTime(timestamp),
            senderId = senderId,
            senderName = senderFullName,
            senderAvatarUrl = avatarUrl,
            reactions = reactions.fromEntitytoChatReactions(currentUserId),
            owner = currentUserId == senderId,
            topic = subject,
        )
    }

    fun List<MessageEntity>.toChatPaginatedMessages(
        currentUserId: Long,
        anchorMessageId: Long?,
        includeAnchorMessage: Boolean
    ): ChatPaginatedMessages {
        return ChatPaginatedMessages(
            messages = asSequence()
                .filter {
                    !(it.id == anchorMessageId && !includeAnchorMessage
                            || it.id == lastOrNull()?.id && !includeAnchorMessage && anchorMessageId == null)
                }
                .map { it.toChatMessage(currentUserId) }.toList(),
            foundAnchor = true,
            foundOldest = true,
            foundNewest = true,
        )
    }

    private fun List<ReactionResponseDto>.fromDtotoChatReactions(currentUserId: Long): List<ChatReaction> {
        return buildList {
            this@fromDtotoChatReactions.filter { it.reactionType == UNICODE_REACTION_TYPE }
                .groupBy { it.emojiName }.asSequence().map { it.value }.forEach {
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

    private fun List<ReactionResponseDto>.toEntityReactions(): List<ReactionEntity> {
        return this@toEntityReactions.filter { it.reactionType == UNICODE_REACTION_TYPE }
            .map {
                ReactionEntity(
                    userId = it.userId,
                    emojiName = it.emojiName,
                    emojiCode = it.emojiCode,
                )
            }

    }

    private fun List<ReactionEntity>.fromEntitytoChatReactions(currentUserId: Long): List<ChatReaction> {
        return buildList {
            this@fromEntitytoChatReactions.groupBy { it.emojiName }.asSequence().map { it.value }.forEach {
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

}