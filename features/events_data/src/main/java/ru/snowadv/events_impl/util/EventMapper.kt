package ru.snowadv.events_impl.util

import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventMessage
import ru.snowadv.events_api.model.EventNarrow
import ru.snowadv.events_api.model.EventPresence
import ru.snowadv.events_api.model.EventReaction
import ru.snowadv.events_api.model.EventStream
import ru.snowadv.events_api.model.EventStreamUpdateFlagsMessages
import ru.snowadv.events_api.model.EventTopicUpdateFlagsMessages
import ru.snowadv.events_api.model.EventType
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
    private fun MessageResponseDto.toEventMessage(currentUserId: Long, flags: List<String>): EventMessage {
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
            flags = flags,
        )
    }

    private fun UserPresenceDto.toEventPresence(serverTimestamp: Double): EventPresence {
        return EventPresence.fromTimestampAndStatus(timestamp.toLong(), status, serverTimestamp.toLong())
    }

    private fun StreamResponseDto.toEventStream(): EventStream {
        return EventStream(id, name, color)
    }

    fun EventResponseDto.toDomainEvent(currentUserId: Long, queueId: String): DomainEvent {
        return when (this) {
            is EventResponseDto.MessageEventDto -> DomainEvent.MessageDomainEvent(
                id = id,
                eventMessage = message.toEventMessage(currentUserId, flags),
                queueId = queueId,
                flags = flags.toSet(),
            )
            is EventResponseDto.HeartbeatEventDto -> DomainEvent.HeartbeatDomainEvent(
                id = id,
                queueId = queueId,
            )
            is EventResponseDto.RealmEventDto -> DomainEvent.RealmDomainEvent(
                id = id,
                op = DomainEvent.RealmDomainEvent.OperationType.valueOf(op.toString()),
                queueId = queueId,
            )
            is EventResponseDto.DeleteMessageEventDto -> DomainEvent.DeleteMessageDomainEvent(
                id = id,
                messageId = messageId,
                queueId = queueId,
                streamId = streamId,
                topic = topic,
                messageType = DomainEvent.DeleteMessageDomainEvent.MessageType.valueOf(messageType.toString()),
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
                op = DomainEvent.ReactionDomainEvent.OperationType.valueOf(op.toString()),
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
                op = DomainEvent.StreamDomainEvent.OperationType.valueOf(op.toString()),
                streams = streams?.map { it.toEventStream() },
                streamName = streamName,
                streamId = streamId,
            )
            is EventResponseDto.TypingEventDto -> DomainEvent.TypingDomainEvent(
                id = id,
                queueId = queueId,
                op = DomainEvent.TypingDomainEvent.OperationType.valueOf(op.toString()),
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
                subject = subject,
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
                op = DomainEvent.UserSubscriptionDomainEvent.OperationType.valueOf(op.toString()),
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


    private fun EventResponseDto.UpdateMessageFlagsEventDto.toAddOrRemoveFlagsEvent(queueId: String): DomainEvent {
        return when {
            op == EventResponseDto.UpdateMessageFlagsEventDto.OperationType.ADD
                    && flag == "read" -> DomainEvent.AddReadMessageFlagEvent(id, messagesIds, queueId)
            op == EventResponseDto.UpdateMessageFlagsEventDto.OperationType.REMOVE
                    && flag == "read" && messageIdToMessageDetails != null -> {
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
            op == EventResponseDto.UpdateMessageFlagsEventDto.OperationType.ADD  -> DomainEvent.AddMessageFlagEvent(id, queueId, flag, messagesIds)
            op == EventResponseDto.UpdateMessageFlagsEventDto.OperationType.REMOVE  -> DomainEvent.RemoveMessageFlagEvent(id, queueId, flag, messagesIds)
            else -> error("Illegal event: $this")
        }
    }

    private fun EventNarrow.toNarrowDto(): NarrowRequestDto {
        return NarrowRequestDto(operator = operator, operand = operand)
    }

    fun List<EventNarrow>.toNarrow2DArrayDto(): Narrow2DArrayRequestDto {
        return Narrow2DArrayRequestDto(map { it.toNarrowDto() })
    }

    fun Set<EventNarrow>.toNarrow2DArrayDto(): Narrow2DArrayRequestDto {
        return Narrow2DArrayRequestDto(map { it.toNarrowDto() })
    }

    private fun List<ReactionResponseDto>.toDataReactionList(currentUserId: Long): List<EventReaction> {
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