package ru.snowadv.channels_data.util

import ru.snowadv.channels_data.model.DataStream
import ru.snowadv.channels_data.model.DataTopic
import ru.snowadv.network.model.StreamDto
import ru.snowadv.network.model.TopicDto

internal fun StreamDto.toDataStream(): DataStream {
    return DataStream(id = id, name = name)
}
internal fun TopicDto.toDataTopic(streamId: Long): DataTopic {
    return DataTopic(
        uniqueId = getUniqueIdForTopic(streamId, name),
        name = name,
        streamId = streamId,
    )
}

private fun getUniqueIdForTopic(streamId: Long, topicName: String) = "${streamId}_${topicName}"