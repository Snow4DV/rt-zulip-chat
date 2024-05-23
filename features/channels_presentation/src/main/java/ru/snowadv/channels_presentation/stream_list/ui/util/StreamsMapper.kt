package ru.snowadv.channels_presentation.stream_list.ui.util

import android.content.Context
import ru.snowadv.channels_presentation.R
import ru.snowadv.channels_domain_api.model.StreamType
import ru.snowadv.channels_presentation.stream_list.ui.model.UiStream
import ru.snowadv.channels_presentation.stream_list.ui.model.UiTopic
import ru.snowadv.channels_domain_api.model.Stream as DomainStream
import ru.snowadv.channels_domain_api.model.Topic as DomainTopic

internal object StreamsMapper {
    fun DomainStream.toUiModel(isExpanded: Boolean): UiStream {
        return UiStream(
            id = id,
            name = name,
            expanded = isExpanded,
            subscribeStatus = getSubscribeStatus(),
            color = color ?: UiStream.DEFAULT_UNREAD_MESSAGES_TEXT_COLOR,
            unreadMessagesCount = unreadMessagesCount,
        )
    }

    private fun DomainStream.getSubscribeStatus(): UiStream.SubscribeStatus {
        return when {
            subscribing -> UiStream.SubscribeStatus.SUBSCRIBING
            subscribed -> UiStream.SubscribeStatus.SUBSCRIBED
            else -> UiStream.SubscribeStatus.NOT_SUBSCRIBED
        }
    }

    fun DomainTopic.toUiModel(unreadMsgsCount: Int = 0): UiTopic {
        return UiTopic(
            uniqueId = uniqueId,
            name = name,
            streamId = streamId,
            unreadMessagesCount = unreadMsgsCount,
        )
    }

    fun UiStream.toDomainModel(): DomainStream {
        return DomainStream(
            id = id,
            name = name,
            subscribing = subscribeStatus == UiStream.SubscribeStatus.SUBSCRIBING,
            subscribed = subscribeStatus == UiStream.SubscribeStatus.SUBSCRIBED,
            color = color,
            unreadMessagesCount = unreadMessagesCount,
        )
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


}