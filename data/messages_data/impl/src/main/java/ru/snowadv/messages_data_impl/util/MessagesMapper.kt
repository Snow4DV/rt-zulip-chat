package ru.snowadv.messages_data_impl.util

import ru.snowadv.database.entity.MessageEntity
import ru.snowadv.database.entity.ReactionEntity
import ru.snowadv.messages_data_api.model.DataMessage
import ru.snowadv.messages_data_api.model.DataPaginatedMessages
import ru.snowadv.messages_data_api.model.DataReaction
import ru.snowadv.messages_data_impl.util.MessagesMapper.toEntityMessage
import ru.snowadv.messages_data_impl.util.MessagesMapper.toEntityReactions
import ru.snowadv.network.model.MessageResponseDto
import ru.snowadv.network.model.MessagesResponseDto
import ru.snowadv.network.model.ReactionResponseDto
import ru.snowadv.utils.DateUtils

internal object MessagesMapper {
    const val UNICODE_REACTION_TYPE = "unicode_emoji"

    private fun MessageResponseDto.toDataMessage(currentUserId: Long): DataMessage {
        return DataMessage(
            id = id,
            content = content,
            sentAt = DateUtils.epochSecondsToZonedDateTime(timestamp),
            senderId = senderId,
            senderName = senderFullName,
            senderAvatarUrl = avatarUrl,
            reactions = reactions.toDataReactions(currentUserId),
            owner = currentUserId == senderId,
        )
    }

    fun MessagesResponseDto.toDataPaginatedMessages(
        currentUserId: Long,
        anchorMessageId: Long?,
        includeAnchorMessage: Boolean
    ): DataPaginatedMessages {
        return DataPaginatedMessages(
            messages = messages.asSequence()
                .filter {
                    !(it.id == anchorMessageId && !includeAnchorMessage
                            || it.id == messages.lastOrNull()?.id && !includeAnchorMessage && anchorMessageId == null)
                }
                .map { it.toDataMessage(currentUserId) }.toList(),
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



    private fun MessageEntity.toDataMessage(currentUserId: Long): DataMessage {
        return DataMessage(
            id = id,
            content = content,
            sentAt = DateUtils.epochSecondsToZonedDateTime(timestamp),
            senderId = senderId,
            senderName = senderFullName,
            senderAvatarUrl = avatarUrl,
            reactions = reactions.toDataReactionList(currentUserId),
            owner = currentUserId == senderId,
        )
    }

    fun List<MessageEntity>.toDataPaginatedMessages(
        currentUserId: Long,
        anchorMessageId: Long?,
        includeAnchorMessage: Boolean
    ): DataPaginatedMessages {
        return DataPaginatedMessages(
            messages = asSequence()
                .filter {
                    !(it.id == anchorMessageId && !includeAnchorMessage
                            || it.id == lastOrNull()?.id && !includeAnchorMessage && anchorMessageId == null)
                }
                .map { it.toDataMessage(currentUserId) }.toList(),
            foundAnchor = true,
            foundOldest = true,
            foundNewest = true,
        )
    }

    private fun List<ReactionResponseDto>.toDataReactions(currentUserId: Long): List<DataReaction> {
        return buildList {
            this@toDataReactions.filter { it.reactionType == UNICODE_REACTION_TYPE }.groupBy { it.emojiName }.asSequence().map { it.value }.forEach {
                add(
                    DataReaction(
                        emojiName = it.first().emojiName,
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
    }

    private fun List<ReactionEntity>.toDataReactionList(currentUserId: Long): List<DataReaction> {
        return buildList {
            this@toDataReactionList.groupBy { it.emojiName }.asSequence().map { it.value }.forEach {
                add(
                    DataReaction(
                        emojiName = it.first().emojiName,
                        emojiCode = it.first().emojiCode,
                        count = it.size,
                        userReacted = it.any { reaction -> reaction.userId == currentUserId },
                    )
                )
            }
        }
    }
