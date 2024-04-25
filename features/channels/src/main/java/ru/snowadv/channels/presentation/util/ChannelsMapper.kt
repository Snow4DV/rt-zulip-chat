package ru.snowadv.channels.presentation.util

import android.content.Context
import ru.snowadv.channels.R
import ru.snowadv.channels.domain.model.StreamType
import ru.snowadv.channels.presentation.model.Stream
import ru.snowadv.channels.presentation.model.StreamUnreadMessages
import ru.snowadv.channels.presentation.model.Topic
import ru.snowadv.channels.presentation.model.TopicUnreadMessages
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
}