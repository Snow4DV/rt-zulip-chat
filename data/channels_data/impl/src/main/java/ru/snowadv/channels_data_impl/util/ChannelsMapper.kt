package ru.snowadv.channels_data_impl.util

import ru.snowadv.channels_data_api.model.DataStream
import ru.snowadv.channels_data_api.model.DataTopic
import ru.snowadv.database.entity.StreamEntity
import ru.snowadv.database.entity.TopicEntity
import ru.snowadv.network.model.StreamResponseDto
import ru.snowadv.network.model.TopicResponseDto

internal object ChannelsMapper {
    fun StreamResponseDto.toDataStream(): DataStream {
        return DataStream(id = id, name = name)
    }

    fun StreamResponseDto.toStreamEntity(subscribed: Boolean): StreamEntity {
        return StreamEntity(id = id, name = name, subscribed = subscribed)
    }

    fun TopicResponseDto.toDataTopic(streamId: Long): DataTopic {
        return DataTopic(
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

    fun StreamEntity.toDataStream(): DataStream {
        return DataStream(id = id, name = name)
    }

    fun TopicEntity.toDataTopic(): DataTopic {
        return DataTopic(uniqueId = uniqueId, name = name, streamId = streamId)
    }

    private fun getUniqueIdForTopic(streamId: Long, topicName: String) = "${streamId}_${topicName}"
}