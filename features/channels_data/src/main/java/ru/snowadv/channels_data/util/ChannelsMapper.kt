package ru.snowadv.channels_data.util

import ru.snowadv.channels_domain_api.model.Stream
import ru.snowadv.channels_domain_api.model.Topic
import ru.snowadv.database.entity.StreamEntity
import ru.snowadv.database.entity.TopicEntity
import ru.snowadv.network.model.StreamResponseDto
import ru.snowadv.network.model.TopicResponseDto

internal object ChannelsMapper {
    fun StreamResponseDto.toDomainStream(subscribed: Boolean): Stream {
        return Stream(id = id, name = name, subscribed = subscribed, subscribing = false, color = color)
    }

    fun StreamResponseDto.toStreamEntity(subscribed: Boolean): StreamEntity {
        return StreamEntity(id = id, name = name, subscribed = subscribed, color = color)
    }

    fun Stream.toStreamEntity(): StreamEntity {
        return StreamEntity(id = id, name = name, subscribed = subscribed, color = color)
    }

    fun TopicResponseDto.toDomainTopic(streamId: Long): Topic {
        return Topic(
            uniqueId = getUniqueIdForTopic(streamId, name),
            name = name,
            streamId = streamId,
        )
    }

    fun TopicResponseDto.toTopicEntity(streamId: Long): TopicEntity {
        return TopicEntity(
            uniqueId = getUniqueIdForTopic(streamId, name),
            name = name,
            streamId = streamId,
        )
    }

    fun StreamEntity.toDomainStream(): Stream {
        return Stream(id = id, name = name, subscribed = subscribed, subscribing = false, color = color)
    }

    fun TopicEntity.toDomainTopic(): Topic {
        return Topic(uniqueId = uniqueId, name = name, streamId = streamId)
    }

    private fun getUniqueIdForTopic(streamId: Long, topicName: String) = "${streamId}_${topicName}"
}