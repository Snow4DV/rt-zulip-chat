package ru.snowadv.message_data.util

import ru.snowadv.message_data.model.DataMessage
import ru.snowadv.message_data.model.DataReaction
import ru.snowadv.network.model.MessageDto
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