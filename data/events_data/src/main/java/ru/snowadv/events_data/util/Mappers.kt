package ru.snowadv.events_data.util

import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.event_api.model.EventMessage
import ru.snowadv.event_api.model.EventNarrow
import ru.snowadv.event_api.model.EventPresence
import ru.snowadv.event_api.model.EventReaction
import ru.snowadv.event_api.model.EventStream
import ru.snowadv.event_api.model.EventType
import ru.snowadv.network.model.EventDto
import ru.snowadv.network.model.EventTypesDto
import ru.snowadv.network.model.MessageDto
import ru.snowadv.network.model.Narrow2DArrayDto
import ru.snowadv.network.model.NarrowDto
import ru.snowadv.network.model.ReactionDto
import ru.snowadv.network.model.StreamDto
import ru.snowadv.network.model.UserPresenceDto
import ru.snowadv.utils.DateUtils


internal fun MessageDto.toEventMessage(currentUserId: Long): EventMessage {
    return EventMessage(
        id = id,
        content = content,
        sentAt = DateUtils.epochSecondsToZonedDateTime(timestamp),
        senderId = senderId,
        senderFullName = senderFullName,
        avatarUrl = avatarUrl,
        reactions = reactions.toDataReactionList(currentUserId),
        owner = currentUserId == senderId,
    )
}

internal fun UserPresenceDto.toEventPresence(serverTimestamp: Double): EventPresence {
    return EventPresence.fromTimestampAndStatus(timestamp.toLong(), status, serverTimestamp.toLong())
}

internal fun StreamDto.toEventStream(): EventStream {
    return EventStream(id, name)
}

internal fun EventDto.toDataEvent(currentUserId: Long): DomainEvent {
    return when (this) {
        is EventDto.MessageEventDto -> DomainEvent.MessageDomainEvent(id, message.toEventMessage(currentUserId))
        is EventDto.HeartbeatEventDto -> DomainEvent.HeartbeatDomainEvent(id)
        is EventDto.RealmEventDto -> DomainEvent.RealmDomainEvent(id, op)
        is EventDto.DeleteMessageEventDto -> DomainEvent.DeleteMessageDomainEvent(id, messageId)
        is EventDto.PresenceEventDto -> DomainEvent.PresenceDomainEvent(id, presence.website.toEventPresence(serverTimestamp), userId, email, userId == currentUserId)
        is EventDto.ReactionEventDto -> DomainEvent.ReactionDomainEvent(id, op, emojiCode, emojiName, messageId, reactionType, userId, userId == currentUserId)
        is EventDto.StreamEventDto -> DomainEvent.StreamDomainEvent(id, op, streams?.map { it.toEventStream() })
        is EventDto.TypingEventDto -> DomainEvent.TypingDomainEvent(id, op, messageType, streamId, topic, sender.userId, sender.email)
        is EventDto.UpdateMessageEventDto -> DomainEvent.UpdateMessageDomainEvent(id, messageId, content)
        is EventDto.UserStatusEventDto -> DomainEvent.UserStatusDomainEvent(id, emojiCode, emojiName, reactionType, statusText, userId)
        is EventDto.UserSubscriptionEventDto -> DomainEvent.UserSubscriptionDomainEvent(id, op, subscriptions?.map { it.toEventStream() })
        is EventDto.UpdateMessageFlagsEventDto -> DomainEvent.UpdateMessageFlagsEvent(id, flag, op, messagesIds)
    }
}

internal fun List<EventType>.toEventTypesDto(): EventTypesDto {
    return EventTypesDto(map { it.apiName })
}

internal fun EventNarrow.toNarrowDto(): NarrowDto {
    return NarrowDto(operator = operator, operand = operand)
}

internal fun List<EventNarrow>.toNarrow2DArrayDto(): Narrow2DArrayDto {
    return Narrow2DArrayDto(map { it.toNarrowDto() })
}

internal fun List<ReactionDto>.toDataReactionList(currentUserId: Long): List<EventReaction> {
    return buildList {
        this@toDataReactionList.groupBy { it.emojiName }.asSequence().map { it.value }.forEach {
            add(
                EventReaction(
                    name = it.first().emojiName,
                    emojiCode = it.first().emojiCode,
                    count = it.size,
                    reactionType = it.first().reactionType,
                    userReacted = it.any { reaction -> reaction.userId == currentUserId },
                )
            )
        }
    }
}