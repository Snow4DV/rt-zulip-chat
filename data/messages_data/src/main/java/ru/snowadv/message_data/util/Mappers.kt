package ru.snowadv.message_data.util

import ru.snowadv.message_data.model.DataMessage
import ru.snowadv.message_data.model.DataReaction
import ru.snowadv.network.model.MessageDto
import ru.snowadv.network.model.ReactionDto
import ru.snowadv.utils.DateUtils

internal fun MessageDto.toDataMessage(): DataMessage {
    return DataMessage(
        id = id,
        content = content,
        sentAt = DateUtils.epochSecondsToZonedDateTime(timestamp),
        senderId = senderId,
        senderName = senderFullName,
        senderAvatarUrl = avatarUrl,
        reactions = reactions.map { it.toDataReaction() })
}

internal fun ReactionDto.toDataReaction(): DataReaction {
    return DataReaction(
        userId = userId,
        emojiName = emojiName,
        emojiCode = emojiCode.toInt(radix = 16),
        reactionType = reactionType
    )
}