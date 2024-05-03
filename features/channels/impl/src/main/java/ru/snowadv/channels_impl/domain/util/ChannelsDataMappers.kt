package ru.snowadv.channels_impl.domain.util

import ru.snowadv.channels_impl.domain.model.Stream
import ru.snowadv.channels_impl.domain.model.Topic
import ru.snowadv.channels_data_api.model.DataStream
import ru.snowadv.channels_data_api.model.DataTopic

object ChannelsDataMappers {
    internal fun DataStream.toChannelStream(): Stream {
        return Stream(id, name)
    }

    internal fun DataTopic.toChannelTopic(): Topic {
        return Topic(uniqueId, name, streamId)
    }
}