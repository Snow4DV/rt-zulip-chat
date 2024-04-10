package ru.snowadv.channels.presentation.util

import android.content.Context
import ru.snowadv.channels.R
import ru.snowadv.channels.domain.model.StreamType
import ru.snowadv.channels.presentation.model.Stream
import ru.snowadv.channels.presentation.model.Topic
import ru.snowadv.channels.domain.model.Stream as DomainStream
import ru.snowadv.channels.domain.model.Topic as DomainTopic

internal fun DomainStream.toUiModel(expandedStreamId: Long? = null): Stream {
    return Stream(
        id = id,
        name = name,
        expanded = id == expandedStreamId
    )
}

internal fun DomainTopic.toUiModel(position: Int): Topic {
    return Topic(
        uniqueId = uniqueId,
        name = name,
        streamId = streamId,
        position = position,
    )
}

internal fun List<DomainTopic>.toUiModelListWithPositions(): List<Topic> {
    return mapIndexed { index, topic -> topic.toUiModel(index) }
}

internal fun StreamType.toLocalizedString(context: Context): String {
    return when(this) {
        StreamType.SUBSCRIBED -> {
            context.getString(R.string.subscribed)
        }
        StreamType.ALL -> {
            context.getString(R.string.all_streams)
        }
    }
}
