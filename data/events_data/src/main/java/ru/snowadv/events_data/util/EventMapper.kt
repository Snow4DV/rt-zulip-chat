package ru.snowadv.events_data.util

import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.event_api.model.EventMessage
import ru.snowadv.event_api.model.EventNarrow
import ru.snowadv.event_api.model.EventPresence
import ru.snowadv.event_api.model.EventReaction
import ru.snowadv.event_api.model.EventStream
import ru.snowadv.event_api.model.EventStreamUpdateFlagsMessages
import ru.snowadv.event_api.model.EventTopicUpdateFlagsMessages
import ru.snowadv.event_api.model.EventType
import ru.snowadv.network.model.EventResponseDto
import ru.snowadv.network.model.EventTypesRequestDto
import ru.snowadv.network.model.MessageResponseDto
import ru.snowadv.network.model.Narrow2DArrayRequestDto
import ru.snowadv.network.model.NarrowRequestDto
import ru.snowadv.network.model.ReactionResponseDto
import ru.snowadv.network.model.StreamResponseDto
import ru.snowadv.network.model.StreamTopicUnreadMessagesResponseDto
import ru.snowadv.network.model.UserPresenceDto
import ru.snowadv.utils.DateUtils


internal object EventMapper {
    fun MessageResponseDto.toEventMessage(currentUserId: Long): EventMessage {
        return EventMessage(
            id = id,
            content = content,
            sentAt = DateUtils.epochSecondsToZonedDateTime(timestamp),
            senderId = senderId,
            senderFullName = senderFullName,
            avatarUrl = avatarUrl,
            reactions = reactions.toDataReactionList(currentUserId),
            owner = currentUserId == senderId,
            type = type,
            streamId = streamId,
            subject = subject,
        )
    }

    fun UserPresenceDto.toEventPresence(serverTimestamp: Double): EventPresence {
        return EventPresence.fromTimestampAndStatus(timestamp.toLong(), status, serverTimestamp.toLong())
    }

    fun StreamResponseDto.toEventStream(): EventStream {
        return EventStream(id, name)
    }

    fun EventResponseDto.toDataEvent(currentUserId: Long): DomainEvent {
        return when (this) {
            is EventResponseDto.MessageEventDto -> DomainEvent.MessageDomainEvent(id, message.toEventMessage(currentUserId))
            is EventResponseDto.HeartbeatEventDto -> DomainEvent.HeartbeatDomainEvent(id)
            is EventResponseDto.RealmEventDto -> DomainEvent.RealmDomainEvent(id, op)
            is EventResponseDto.DeleteMessageEventDto -> DomainEvent.DeleteMessageDomainEvent(id, messageId)
            is EventResponseDto.PresenceEventDto -> DomainEvent.PresenceDomainEvent(id, presence.website.toEventPresence(serverTimestamp), userId, email, userId == currentUserId)
            is EventResponseDto.ReactionEventDto -> DomainEvent.ReactionDomainEvent(id, op, emojiCode, emojiName, messageId, reactionType, userId, userId == currentUserId)
            is EventResponseDto.StreamEventDto -> DomainEvent.StreamDomainEvent(id, op, streams?.map { it.toEventStream() }, streamName, streamId)
            is EventResponseDto.TypingEventDto -> DomainEvent.TypingDomainEvent(id, op, messageType, streamId, topic, sender.userId, sender.email)
            is EventResponseDto.UpdateMessageEventDto -> DomainEvent.UpdateMessageDomainEvent(id, messageId, content)
            is EventResponseDto.UserStatusEventDto -> DomainEvent.UserStatusDomainEvent(id, emojiCode, emojiName, reactionType, statusText, userId)
            is EventResponseDto.UserSubscriptionEventDto -> DomainEvent.UserSubscriptionDomainEvent(id, op, subscriptions?.map { it.toEventStream() })
            is EventResponseDto.UpdateMessageFlagsEventDto -> this.toAddOrRemoveFlagsEvent()
        }
    }

    fun List<EventType>.toEventTypesDto(): EventTypesRequestDto {
        return EventTypesRequestDto(map { it.apiName })
    }

    fun EventResponseDto.UpdateMessageFlagsEventDto.toAddOrRemoveFlagsEvent(): DomainEvent {
        val t = messageIdToMessageDetails?.entries
            ?.filter { it.value.streamId != null && it.value.topic != null && it.value.type == "stream" }
            ?.groupBy { it.value.streamId ?: error("Malformed json: streamId is null in $this but type is stream") }
            ?.mapValues { it.value.groupBy { it.value.topic ?: error("Malformed json: topic is null but type is stream") } }
        return when {
            op == "add" && flag == "read" -> DomainEvent.AddReadMessageFlagEvent(id, messagesIds)
            op == "remove" && flag == "read" && messageIdToMessageDetails != null -> {
                DomainEvent.RemoveReadMessageFlagEvent(
                    id = id,
                    removeFlagMessagesIds = messagesIds,
                    removeFlagMessages = messageIdToMessageDetails?.entries
                        ?.filter { it.value.streamId != null && it.value.topic != null && it.value.type == "stream" }
                        ?.groupBy { it.value.streamId ?: error("Malformed json: streamId is null in $this but type is stream") }
                        ?.mapValues { it.value.groupBy { it.value.topic ?: error("Malformed json: topic is null but type is stream") } }
                        ?.map { EventStreamUpdateFlagsMessages(
                            streamId = it.key,
                            topicsUnreadMessages = it.value.map { EventTopicUpdateFlagsMessages(
                                topicName = it.key,
                                unreadMessagesIds = it.value.map { it.key.toLong() }
                            ) }
                        ) } ?: error("Missing message details but type is read")
                )
            }
            op == "add" -> DomainEvent.AddMessageFlagEvent(id, flag, messagesIds)
            op == "remove" -> DomainEvent.RemoveMessageFlagEvent(id, flag, messagesIds)
            else -> error("Illegal event flag = $flag: $this")
        }
    }

    fun EventNarrow.toNarrowDto(): NarrowRequestDto {
        return NarrowRequestDto(operator = operator, operand = operand)
    }

    fun List<EventNarrow>.toNarrow2DArrayDto(): Narrow2DArrayRequestDto {
        return Narrow2DArrayRequestDto(map { it.toNarrowDto() })
    }

    fun List<ReactionResponseDto>.toDataReactionList(currentUserId: Long): List<EventReaction> {
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

    fun StreamTopicUnreadMessagesResponseDto.toEventTopicUnreadMessages(): EventTopicUpdateFlagsMessages {
        return EventTopicUpdateFlagsMessages(topicName, unreadMessagesIds)
    }
}