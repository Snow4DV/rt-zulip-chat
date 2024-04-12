package ru.snowadv.events_data.util

import ru.snowadv.events_data.model.DataEvent
import ru.snowadv.events_data.model.DataEventType
import ru.snowadv.events_data.model.DataMessage
import ru.snowadv.events_data.model.DataNarrow
import ru.snowadv.events_data.model.DataReaction
import ru.snowadv.network.model.EventDto
import ru.snowadv.network.model.MessageDto
import ru.snowadv.network.model.NarrowDto
import ru.snowadv.network.model.ReactionDto
import ru.snowadv.utils.DateUtils

internal fun DataNarrow.toDto(): NarrowDto {
    return NarrowDto(operator, operand)
}

internal fun MessageDto.toDataMessage(currentUserId: Long): DataMessage {
    return DataMessage(
        id = id,
        content = content,
        sentAt = DateUtils.epochSecondsToZonedDateTime(timestamp),
        senderId = senderId,
        senderName = senderFullName,
        senderAvatarUrl = avatarUrl,
        reactions = reactions.toDataReactionList(currentUserId)
    )
}

internal fun EventDto.toDataEvent(currentUserId: Long): DataEvent {
    return when (this) {
        is EventDto.MessageEventDto -> DataEvent.MessageEvent(id, message.toDataMessage(currentUserId))
    }
}

internal fun List<DataEventType>.toStringEventTypes(): List<String> {
    return map { it.apiName }
}

fun List<ReactionDto>.toDataReactionList(currentUserId: Long): List<DataReaction> {
    return buildList {
        this@toDataReactionList.groupBy { it.emojiName }.asSequence().map { it.value }.forEach {
            add(
                DataReaction(
                    emojiName = it.first().emojiName,
                    emojiCode = it.first().emojiCode.toInt(radix = 16), // TODO filter emojis with strange code like "1fff-20ce"
                    count = it.size,
                    reactionType = it.first().reactionType,
                    userReacted = it.any { reaction -> reaction.userId == currentUserId },
                )
            )
        }
    }
}