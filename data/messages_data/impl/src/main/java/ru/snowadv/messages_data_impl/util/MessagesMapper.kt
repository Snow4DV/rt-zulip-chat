package ru.snowadv.messages_data_impl.util

import ru.snowadv.messages_data_api.model.DataMessage
import ru.snowadv.messages_data_api.model.DataPaginatedMessages
import ru.snowadv.messages_data_api.model.DataReaction
import ru.snowadv.network.model.MessageResponseDto
import ru.snowadv.network.model.MessagesResponseDto
import ru.snowadv.network.model.ReactionResponseDto
import ru.snowadv.utils.DateUtils

internal object MessagesMapper {
    private fun MessageResponseDto.toDataMessage(currentUserId: Long): DataMessage {
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

    private fun List<ReactionResponseDto>.toDataReactionList(currentUserId: Long): List<DataReaction> {
        return buildList {
            this@toDataReactionList.groupBy { it.emojiName }.asSequence().map { it.value }.forEach {
                add(
                    DataReaction(
                        emojiName = it.first().emojiName,
                        emojiCode = it.first().emojiCode,
                        count = it.size,
                        reactionType = it.first().reactionType,
                        userReacted = it.any { reaction -> reaction.userId == currentUserId },
                    )
                )
            }
        }
    }
}