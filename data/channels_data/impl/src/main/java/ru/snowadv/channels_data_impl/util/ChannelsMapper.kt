package ru.snowadv.channels_data_impl.util

import ru.snowadv.channels_data_api.model.DataStream
import ru.snowadv.channels_data_api.model.DataTopic
import ru.snowadv.network.model.StreamResponseDto
import ru.snowadv.network.model.TopicResponseDto

internal object ChannelsMapper {
    fun StreamResponseDto.toDataStream(): DataStream {
        return DataStream(id = id, name = name)
    }
    fun TopicResponseDto.toDataTopic(streamId: Long): DataTopic {
        return DataTopic(
            uniqueId = getUniqueIdForTopic(streamId, name),
            name = name,
            streamId = streamId,
        )
    }

    private fun getUniqueIdForTopic(streamId: Long, topicName: String) = "${streamId}_${topicName}"
}