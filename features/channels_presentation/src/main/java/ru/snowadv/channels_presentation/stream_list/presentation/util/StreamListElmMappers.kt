package ru.snowadv.channels_presentation.stream_list.presentation.util

import ru.snowadv.channels_domain_api.model.Stream
import ru.snowadv.channels_domain_api.model.StreamUnreadMessages
import ru.snowadv.channels_domain_api.model.TopicUnreadMessages
import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListEventElm
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventStream
import ru.snowadv.events_api.model.EventStreamUpdateFlagsMessages
import ru.snowadv.events_api.model.EventTopicUpdateFlagsMessages

internal object StreamListElmMappers {
    const val READ_FLAG = "read"
    fun EventTopicUpdateFlagsMessages.toDomainModel(): TopicUnreadMessages {
        return TopicUnreadMessages(topicName = topicName, unreadMessagesIds = unreadMessagesIds)
    }

    fun EventStreamUpdateFlagsMessages.toDomainModel(): StreamUnreadMessages {
        return StreamUnreadMessages(streamId, topicsUnreadMessages.map { it.toDomainModel() })
    }

    fun EventStream.toDomainModel(subscribed: Boolean): Stream {
        return Stream(id = id, name = name, subscribing = false, subscribed = subscribed, color = color)
    }

    private fun DomainEvent.toEventQueueUpdateElmEvent(): StreamListEventElm.Internal.ServerEvent.EventQueueUpdated {
        return StreamListEventElm.Internal.ServerEvent.EventQueueUpdated(queueId = queueId, eventId = id)
    }

    fun DomainEvent.toElmEvent(): StreamListEventElm.Internal.ServerEvent {
        return when(this) {
            is DomainEvent.AddReadMessageFlagEvent -> {
                StreamListEventElm.Internal.ServerEvent.MessagesRead(
                    queueId = queueId,
                    eventId = id,
                    readMessagesIds = addFlagMessagesIds,
                )
            }
            is DomainEvent.DeleteMessageDomainEvent -> {
                this.toElmEvent()
            }
            is DomainEvent.FailedFetchingQueueEvent -> StreamListEventElm.Internal.ServerEvent.EventQueueFailed(
                queueId = queueId,
                eventId = id,
                recreateQueue = isQueueBad,
                reason = reason,
            )
            is DomainEvent.MessageDomainEvent -> {
                toElmEvent()
            }
            is DomainEvent.RegisteredNewQueueEvent -> StreamListEventElm.Internal.ServerEvent.EventQueueRegistered(
                queueId = queueId,
                eventId = id,
                timeoutSeconds = timeoutSeconds,
                streamsUnreadMessages = streamUnreadMessages?.map {
                        updateMsgFlags -> updateMsgFlags.toDomainModel()
                } ?: emptyList(),
            )
            is DomainEvent.RemoveReadMessageFlagEvent -> {
                StreamListEventElm.Internal.ServerEvent.MessagesUnread(
                    queueId = queueId,
                    eventId = id,
                    nowUnreadMessages = removeFlagMessages.map {
                            updateFlagMessages -> updateFlagMessages.toDomainModel()
                    },
                )
            }
            is DomainEvent.StreamDomainEvent -> this.toElmEvent()
            is DomainEvent.UserSubscriptionDomainEvent -> this.toElmEvent()
            else -> this.toEventQueueUpdateElmEvent()
        }
    }

    fun DomainEvent.MessageDomainEvent.toElmEvent(): StreamListEventElm.Internal.ServerEvent {
        val streamId = eventMessage.streamId
        return if (streamId != null) {
            StreamListEventElm.Internal.ServerEvent.NewMessage(
                queueId = queueId,
                eventId = id,
                messageId = eventMessage.id,
                streamId = streamId,
                topicName = eventMessage.subject,
                flags = flags,
                isRead = READ_FLAG in flags,
            )
        } else {
            toEventQueueUpdateElmEvent()
        }
    }

    fun DomainEvent.DeleteMessageDomainEvent.toElmEvent(): StreamListEventElm.Internal.ServerEvent {
        val streamId = streamId
        val topic = topic
        return if (streamId != null && topic != null && messageType == DomainEvent.DeleteMessageDomainEvent.MessageType.STREAM) {
            StreamListEventElm.Internal.ServerEvent.MessageDeleted(
                queueId = queueId,
                eventId = id,
                messageId = messageId,
                streamId = streamId,
                topic = topic,
            )
        } else {
            toEventQueueUpdateElmEvent()
        }
    }

    fun DomainEvent.StreamDomainEvent.toElmEvent(): StreamListEventElm.Internal.ServerEvent {
        return when(op) {
            DomainEvent.StreamDomainEvent.OperationType.CREATE-> {
                StreamListEventElm.Internal.ServerEvent.StreamsAddedEvent(
                    queueId = queueId,
                    eventId = id,
                    streams = streams?.map { it.toDomainModel(subscribed = false) } ?: emptyList(),
                )
            }
            DomainEvent.StreamDomainEvent.OperationType.DELETE -> {
                StreamListEventElm.Internal.ServerEvent.StreamsAddedEvent(
                    queueId = queueId,
                    eventId = id,
                    streams = streams?.map { it.toDomainModel(subscribed = false) } ?: emptyList(),
                )
            }
            DomainEvent.StreamDomainEvent.OperationType.UPDATE -> {
                StreamListEventElm.Internal.ServerEvent.StreamUpdatedEvent(
                    queueId = queueId,
                    eventId = id,
                    streamId = streamId,
                    streamName = streamName,
                )
            }
        }
    }

    fun DomainEvent.UserSubscriptionDomainEvent.toElmEvent(): StreamListEventElm.Internal.ServerEvent {
        return when(op) {
            DomainEvent.UserSubscriptionDomainEvent.OperationType.ADD -> {
                StreamListEventElm.Internal.ServerEvent.UserSubscriptionsAddedEvent(
                    queueId = queueId,
                    eventId = id,
                    changedStreams = subscriptions?.map { eventStream -> eventStream.toDomainModel(subscribed = true) } ?: emptyList(),
                )
            }
            DomainEvent.UserSubscriptionDomainEvent.OperationType.REMOVE -> {
                StreamListEventElm.Internal.ServerEvent.UserSubscriptionsRemovedEvent(
                    queueId = queueId,
                    eventId = id,
                    changedStreams = subscriptions?.map { eventStream -> eventStream.toDomainModel(subscribed = false) } ?: emptyList(),
                )
            }
            else -> this.toEventQueueUpdateElmEvent()
        }
    }
}