package ru.snowadv.channels_presentation.stream_list.presentation.elm

import ru.snowadv.channels_domain_api.model.Stream
import ru.snowadv.channels_domain_api.model.StreamUnreadMessages
import ru.snowadv.channels_domain_api.model.Topic
import ru.snowadv.events_api.model.EventInfoHolder
import ru.snowadv.events_api.model.EventSenderType

internal sealed interface StreamListEventElm {

    sealed interface Ui : StreamListEventElm {
        data object Init : Ui
        data object Resumed : Ui
        data object Paused : Ui
        data class ClickedOnTopic(val topicName: String) : Ui
        data class ClickedOnExpandStream(val streamId: Long) : Ui
        data class ClickedOnOpenStream(val streamId: Long, val streamName: String) : Ui
        data class ClickedOnChangeStreamSubscriptionStatus(val stream: Stream) : Ui
        data class ChangedQuery(val query: String) : Ui
        data object ReloadClicked : Ui
    }

    sealed interface Internal : StreamListEventElm {
        data class StreamsLoaded(val streams: List<Stream>, val cached: Boolean) : Internal
        data class Error(val throwable: Throwable, val cachedStreams: List<Stream>?) : Internal
        data object Loading : Internal
        data class TopicsLoading(val streamId: Long) : Internal
        data class TopicsLoadingError(val streamId: Long, val throwable: Throwable) : Internal
        data class TopicsLoadingErrorWithCachedTopics(val streamId: Long, val throwable: Throwable, val cachedTopics: List<Topic>) :
            Internal
        data class TopicsLoaded(val streamId: Long, val topics: List<Topic>) : Internal

        data class SubscribingToStream(val streamId: Long) : Internal
        data class ErrorWhileSubscribingToStream(val streamId: Long, val throwable: Throwable) : Internal
        data class ChangedSubscriptionToStream(val streamId: Long, val isSubscribed: Boolean) : Internal

        sealed class ServerEvent : Internal, EventInfoHolder {
            data class EventQueueRegistered(
                override val queueId: String,
                override val eventId: Long,
                val timeoutSeconds: Int,
                val streamsUnreadMessages: List<StreamUnreadMessages>,
            ) : ServerEvent() {
                override val senderType: EventSenderType
                    get() = EventSenderType.SYNTHETIC_REGISTER
            }

            data class EventQueueFailed(
                override val queueId: String?,
                override val eventId: Long,
                val recreateQueue: Boolean,
                val reason: Throwable,
            ) : ServerEvent() {
                override val senderType: EventSenderType
                    get() = EventSenderType.SYNTHETIC_FAIL
            }


            data class EventQueueUpdated(
                override val queueId: String?,
                override val eventId: Long,
            ) : ServerEvent()

            data class NewMessage(
                override val queueId: String?,
                override val eventId: Long,
                val streamId: Long,
                val topicName: String,
                val messageId: Long,
                val flags: Set<String>,
                val isRead: Boolean,
            ) : ServerEvent()

            data class MessageDeleted(
                override val queueId: String?,
                override val eventId: Long,
                val messageId: Long,
                val streamId: Long,
                val topic: String,
            ) : ServerEvent()

            data class MessagesRead(
                override val queueId: String?,
                override val eventId: Long,
                val readMessagesIds: List<Long>
            ) : ServerEvent()

            data class MessagesUnread(
                override val queueId: String?,
                override val eventId: Long,
                val nowUnreadMessages: List<StreamUnreadMessages>,
            ) : ServerEvent()

            data class UserSubscriptionsAddedEvent(
                override val queueId: String?,
                override val eventId: Long,
                val changedStreams: List<Stream>,
            ) : ServerEvent()

            data class UserSubscriptionsRemovedEvent(
                override val queueId: String?,
                override val eventId: Long,
                val changedStreams: List<Stream>,
            ) : ServerEvent()

            data class StreamsAddedEvent(
                override val queueId: String?,
                override val eventId: Long,
                val streams: List<Stream>,
            ) : ServerEvent()

            data class StreamsRemovedEvent(
                override val queueId: String?,
                override val eventId: Long,
                val streams: List<Stream>,
            ) : ServerEvent()

            data class StreamUpdatedEvent(
                override val queueId: String?,
                override val eventId: Long,
                val streamId: Long?,
                val streamName: String?,
            ) : ServerEvent()
        }
    }
}