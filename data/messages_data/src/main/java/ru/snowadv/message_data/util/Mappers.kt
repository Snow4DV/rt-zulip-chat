package ru.snowadv.message_data.util

import ru.snowadv.message_data.model.DataMessage
import ru.snowadv.message_data.model.DataPaginatedMessages
import ru.snowadv.message_data.model.DataReaction
import ru.snowadv.network.model.MessageDto
import ru.snowadv.network.model.MessagesDto
import ru.snowadv.network.model.ReactionDto
import ru.snowadv.utils.DateUtils

internal fun MessageDto.toDataMessage(currentUserId: Long): DataMessage {
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

internal fun MessagesDto.toDataPaginatedMessages(
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

fun List<ReactionDto>.toDataReactionList(currentUserId: Long): List<DataReaction> {
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