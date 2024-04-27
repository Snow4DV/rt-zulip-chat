package ru.snowadv.voiceapp.glue.util

import ru.snowadv.channels.domain.model.Stream
import ru.snowadv.channels.domain.model.Topic
import ru.snowadv.channels_data.model.DataStream
import ru.snowadv.channels_data.model.DataTopic

object ChannelsDataMappers {
    internal fun DataStream.toChannelStream(): Stream {
        return Stream(id, name)
    }

    internal fun DataTopic.toChannelTopic(): Topic {
        return Topic(uniqueId, name, streamId)
    }
}