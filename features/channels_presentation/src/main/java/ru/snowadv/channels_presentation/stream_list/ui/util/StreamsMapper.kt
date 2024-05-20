package ru.snowadv.channels_presentation.stream_list.ui.util

import android.content.Context
import ru.snowadv.channels_presentation.R
import ru.snowadv.channels_domain_api.model.StreamType
import ru.snowadv.channels_presentation.stream_list.ui.model.UiStream
import ru.snowadv.channels_presentation.stream_list.ui.model.UiTopic
import ru.snowadv.channels_domain_api.model.Stream as DomainStream
import ru.snowadv.channels_domain_api.model.Topic as DomainTopic

internal object StreamsMapper {
    fun DomainStream.toUiModel(expandedStreamId: Long? = null): UiStream {
        return UiStream(
            id = id,
            name = name,
            expanded = id == expandedStreamId
        )
    }

    fun DomainStream.toUiModel(isExpanded: Boolean): UiStream {
        return UiStream(
            id = id,
            name = name,
            expanded = isExpanded,
        )
    }

    fun DomainTopic.toUiModel(): UiTopic {
        return UiTopic(
            uniqueId = uniqueId,
            name = name,
            streamId = streamId,
            unreadMessagesCount = 0,
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