package ru.snowadv.channels.presentation.util

import android.content.Context
import ru.snowadv.channels.R
import ru.snowadv.channels.domain.model.StreamType
import ru.snowadv.channels.presentation.model.Stream
import ru.snowadv.channels.presentation.model.StreamUnreadMessages
import ru.snowadv.channels.presentation.model.Topic
import ru.snowadv.channels.presentation.model.TopicUnreadMessages
import ru.snowadv.channels.presentation.stream_list.elm.StreamListEventElm
import ru.snowadv.channels.presentation.util.ChannelsMapper.toUiModel
import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.event_api.model.EventStream
import ru.snowadv.event_api.model.EventStreamUpdateFlagsMessages
import ru.snowadv.event_api.model.EventTopicUpdateFlagsMessages
import ru.snowadv.channels.domain.model.Stream as DomainStream
import ru.snowadv.channels.domain.model.Topic as DomainTopic

internal object ChannelsMapper {
    fun DomainStream.toUiModel(expandedStreamId: Long? = null): Stream {
        return Stream(
            id = id,
            name = name,
            expanded = id == expandedStreamId
        )
    }

    fun DomainTopic.toUiModel(): Topic {
        return Topic(
            uniqueId = uniqueId,
            name = name,
            streamId = streamId,
            unreadMessagesCount = 0,
        )
    }

    fun List<DomainTopic>.toUiModelListWithPositions(): List<Topic> {
        return map { topic -> topic.toUiModel() }
    }

    fun StreamType.toLocalizedString(context: Context): String {
        return when(this) {
            StreamType.SUBSCRIBED -> {
                context.getString(R.string.subscribed)
            }
            StreamType.ALL -> {
                context.getString(R.string.all_streams)
            }
        }
    }

    fun EventTopicUpdateFlagsMessages.toUiModel(): TopicUnreadMessages {
        return TopicUnreadMessages(topicName = topicName, unreadMessagesIds = unreadMessagesIds)
    }

    fun EventStreamUpdateFlagsMessages.toUiModel(): StreamUnreadMessages {
        return StreamUnreadMessages(streamId, topicsUnreadMessages.map { it.toUiModel() })
    }

    fun EventStream.toUiModel(expanded: Boolean = false): Stream {
        return Stream(id, name, expanded)
    }

    fun DomainEvent.toEventQueueUpdateElmEvent(): StreamListEventElm.Internal.ServerEvent.EventQueueUpdated {
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
                StreamListEventElm.Internal.ServerEvent.MessageDeleted(
                    queueId = queueId,
                    eventId = id,
                    messageId = messageId,
                )
            }
            is DomainEvent.FailedFetchingQueueEvent -> StreamListEventElm.Internal.ServerEvent.EventQueueFailed(
                queueId = queueId,
                eventId = id,
                recreateQueue = isQueueBad,
            )
            is DomainEvent.MessageDomainEvent -> {
                StreamListEventElm.Internal.ServerEvent.NewMessage(
                    queueId = queueId,
                    eventId = id,
                    messageId = eventMessage.id,
                    streamId = eventMessage.streamId,
                    topicName = eventMessage.subject,
                )
            }
            is DomainEvent.RegisteredNewQueueEvent -> StreamListEventElm.Internal.ServerEvent.EventQueueRegistered(
                queueId = queueId,
                eventId = id,
                timeoutSeconds = timeoutSeconds,
                streamsUnreadMessages = streamUnreadMessages?.map {
                        updateMsgFlags -> updateMsgFlags.toUiModel()
                } ?: error ("Wrong event queue registered. Check event types in use case"),
            )
            is DomainEvent.RemoveReadMessageFlagEvent -> {
                StreamListEventElm.Internal.ServerEvent.MessagesUnread(
                    queueId = queueId,
                    eventId = id,
                    nowUnreadMessages = removeFlagMessages.map {
                            updateFlagMessages -> updateFlagMessages.toUiModel()
                    },
                )
            }
            is DomainEvent.StreamDomainEvent -> this.toElmEvent()
            is DomainEvent.UserSubscriptionDomainEvent -> this.toElmEvent()
            else -> this.toEventQueueUpdateElmEvent()
        }
    }

    fun DomainEvent.StreamDomainEvent.toElmEvent(): StreamListEventElm.Internal.ServerEvent {
        return when(op) {
            "create" -> {
                StreamListEventElm.Internal.ServerEvent.StreamsAddedEvent(
                    queueId = queueId,
                    eventId = id,
                    streams = streams?.map { it.toUiModel() } ?: emptyList(),
                )
            }
            "delete" -> {
                StreamListEventElm.Internal.ServerEvent.StreamsAddedEvent(
                    queueId = queueId,
                    eventId = id,
                    streams = streams?.map { it.toUiModel() } ?: emptyList(),
                )
            }
            "update" -> {
                StreamListEventElm.Internal.ServerEvent.StreamUpdatedEvent(
                    queueId = queueId,
                    eventId = id,
                    streamId = streamId,
                    streamName = streamName,
                )
            }
            else -> error("Unsupported event: stream event with operation = \"$op\"")
        }
    }

    fun DomainEvent.UserSubscriptionDomainEvent.toElmEvent(): StreamListEventElm.Internal.ServerEvent {
        return when(op) {
            "add" -> {
                StreamListEventElm.Internal.ServerEvent.UserSubscriptionsAddedEvent(
                    queueId = queueId,
                    eventId = id,
                    changedStreams = subscriptions?.map { eventStream -> eventStream.toUiModel() } ?: emptyList(),
                )
            }
            "remove" -> {
                StreamListEventElm.Internal.ServerEvent.UserSubscriptionsRemovedEvent(
                    queueId = queueId,
                    eventId = id,
                    changedStreams = subscriptions?.map { eventStream -> eventStream.toUiModel() } ?: emptyList(),
                )
            }
            else -> error("Unsupported op for subscription event: \"$op\"")
        }
    }
}