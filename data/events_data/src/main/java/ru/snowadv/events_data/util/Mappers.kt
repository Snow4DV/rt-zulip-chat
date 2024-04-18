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

internal fun ReactionDto.toDataReaction(): DataReaction {
    return DataReaction(userId, emojiName, emojiCode.toInt(radix = 16), reactionType) // TODO filter emojis with strange code
}

internal fun MessageDto.toDataMessage(): DataMessage {
    return DataMessage(
        id = id,
        content = content,
        sentAt = DateUtils.epochSecondsToZonedDateTime(timestamp),
        senderId = senderId,
        senderName = senderFullName,
        senderAvatarUrl = avatarUrl,
        reactions = reactions.map { it.toDataReaction() },
    )
}

internal fun EventDto.toDataEvent(): DataEvent {
    return when (this) {
        is EventDto.MessageEventDto -> DataEvent.MessageEvent(id, message.toDataMessage())
    }
}

internal fun List<DataEventType>.toStringEventTypes(): List<String> {
    return map { it.apiName }
}