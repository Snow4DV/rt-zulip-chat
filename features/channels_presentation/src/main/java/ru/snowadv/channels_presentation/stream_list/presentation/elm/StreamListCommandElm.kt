package ru.snowadv.channels_presentation.stream_list.presentation.elm

import ru.snowadv.channels_domain_api.model.StreamType
import ru.snowadv.events_api.model.EventQueueProperties

internal sealed interface StreamListCommandElm {
    data class ChangeSubscriptionStatusForStream(val streamId: Long, val streamName: String, val subscribe: Boolean) : StreamListCommandElm
    data class LoadStreams(val type: StreamType) :
        StreamListCommandElm
    data class LoadTopics(val streamId: Long) : StreamListCommandElm
    data class ObserveEvents(
        val isRestart: Boolean,
        val queueProps: EventQueueProperties?,
    ) : StreamListCommandElm
    data class GoToTopic(val streamId: Long, val streamName: String, val topicName: String) : StreamListCommandElm
    data class GoToStream(val streamId: Long, val streamName: String) : StreamListCommandElm
}