package ru.snowadv.home.presentation.util

import android.content.Context
import ru.snowadv.home.R
import ru.snowadv.home.domain.model.StreamType
import ru.snowadv.home.presentation.model.Person
import ru.snowadv.home.presentation.model.Stream
import ru.snowadv.home.presentation.model.Topic
import ru.snowadv.home.domain.model.Stream as DomainStream
import ru.snowadv.home.domain.model.Topic as DomainTopic
import ru.snowadv.home.domain.model.Person as DomainPerson
import ru.snowadv.home.domain.model.Person.Status as DomainPersonStatus

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

internal fun DomainPersonStatus.toUiModel(): Person.Status {
    return Person.Status.entries[ordinal]
}

internal fun DomainPerson.toUiModel(): Person {
    return Person(id, fullName, email, avatarUrl, status.toUiModel())
}