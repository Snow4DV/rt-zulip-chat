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
import ru.snowadv.events_data.util.EventMapper.toEventTopicUnreadMessages
import ru.snowadv.events_data.util.EventMapper.toRegisteredQueueEvent
import ru.snowadv.network.model.EventQueueResponseDto
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
            flags = flags.toSet(),
        )
    }

    fun UserPresenceDto.toEventPresence(serverTimestamp: Double): EventPresence {
        return EventPresence.fromTimestampAndStatus(timestamp.toLong(), status, serverTimestamp.toLong())
    }

    fun StreamResponseDto.toEventStream(): EventStream {
        return EventStream(id, name)
    }

    fun EventResponseDto.toDataEvent(currentUserId: Long, queueId: String): DomainEvent {
        return when (this) {
            is EventResponseDto.MessageEventDto -> DomainEvent.MessageDomainEvent(
                id = id,
                eventMessage = message.toEventMessage(currentUserId),
                queueId = queueId,
            )
            is EventResponseDto.HeartbeatEventDto -> DomainEvent.HeartbeatDomainEvent(
                id = id,
                queueId = queueId,
            )
            is EventResponseDto.RealmEventDto -> DomainEvent.RealmDomainEvent(
                id = id,
                op = op,
                queueId = queueId,
            )
            is EventResponseDto.DeleteMessageEventDto -> DomainEvent.DeleteMessageDomainEvent(
                id = id,
                messageId = messageId,
                queueId = queueId,
            )
            is EventResponseDto.PresenceEventDto -> DomainEvent.PresenceDomainEvent(
                id = id,
                queueId = queueId,
                presence = presence.website.toEventPresence(serverTimestamp),
                userId = userId,
                email = email,
                currentUser = userId == currentUserId,
                )
            is EventResponseDto.ReactionEventDto -> DomainEvent.ReactionDomainEvent(
                id = id,
                queueId = queueId,
                op = op,
                emojiCode = emojiCode,
                emojiName = emojiName,
                messageId = messageId,
                reactionType = reactionType,
                userId = userId,
                currentUserReaction = userId == currentUserId,
            )
            is EventResponseDto.StreamEventDto -> DomainEvent.StreamDomainEvent(
                id = id,
                queueId = queueId,
                op = op,
                streams = streams?.map { it.toEventStream() },
                streamName = streamName,
                streamId = streamId,
            )
            is EventResponseDto.TypingEventDto -> DomainEvent.TypingDomainEvent(
                id = id,
                queueId = queueId,
                op = op,
                messageType = messageType,
                streamId = streamId,
                topic = topic,
                userId = sender.userId,
                userEmail = sender.email,
            )
            is EventResponseDto.UpdateMessageEventDto -> DomainEvent.UpdateMessageDomainEvent(
                id = id,
                messageId = messageId,
                content = content,
                queueId = queueId,
            )
            is EventResponseDto.UserStatusEventDto -> DomainEvent.UserStatusDomainEvent(
                id = id,
                queueId = queueId,
                emojiCode = emojiCode,
                emojiName = emojiName,
                reactionType = reactionType,
                statusText = statusText,
                userId = userId,
            )
            is EventResponseDto.UserSubscriptionEventDto -> DomainEvent.UserSubscriptionDomainEvent(
                id = id,
                queueId = queueId,
                op = op,
                subscriptions = subscriptions?.map { it.toEventStream() },
            )
            is EventResponseDto.UpdateMessageFlagsEventDto -> this.toAddOrRemoveFlagsEvent(queueId)
        }
    }

    fun List<EventType>.toEventTypesDto(): EventTypesRequestDto {
        return EventTypesRequestDto(map { it.apiName })
    }

    fun Set<EventType>.toEventTypesDto(): EventTypesRequestDto {
        return EventTypesRequestDto(map { it.apiName })
    }


    fun EventResponseDto.UpdateMessageFlagsEventDto.toAddOrRemoveFlagsEvent(queueId: String): DomainEvent {
        return when {
            op == "add" && flag == "read" -> DomainEvent.AddReadMessageFlagEvent(id, messagesIds, queueId)
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
                        ) } ?: error("Missing message details but type is read"),
                    queueId = queueId,
                )
            }
            op == "add" -> DomainEvent.AddMessageFlagEvent(id, queueId, flag, messagesIds)
            op == "remove" -> DomainEvent.RemoveMessageFlagEvent(id, queueId, flag, messagesIds)
            else -> error("Illegal event flag = $flag: $this")
        }
    }

    fun EventNarrow.toNarrowDto(): NarrowRequestDto {
        return NarrowRequestDto(operator = operator, operand = operand)
    }

    fun List<EventNarrow>.toNarrow2DArrayDto(): Narrow2DArrayRequestDto {
        return Narrow2DArrayRequestDto(map { it.toNarrowDto() })
    }

    fun Set<EventNarrow>.toNarrow2DArrayDto(): Narrow2DArrayRequestDto {
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

    fun List<StreamTopicUnreadMessagesResponseDto>.toEventStreamMessages(): List<EventStreamUpdateFlagsMessages> {
        return groupBy { it.streamId }.map {
            EventStreamUpdateFlagsMessages(
                it.key,
                it.value.map { streamTopicUnreadMessagesDto -> streamTopicUnreadMessagesDto.toEventTopicUnreadMessages() })
        }
    }

    fun EventQueueResponseDto.toRegisteredQueueEvent(): DomainEvent.RegisteredNewQueueEvent {
        return DomainEvent.RegisteredNewQueueEvent(queueId, lastEventId, longPollTimeoutSeconds, unreadMessages?.streams?.toEventStreamMessages())
    }
}