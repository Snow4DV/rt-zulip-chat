package ru.snowadv.channels_impl.presentation.stream_list.elm

import ru.snowadv.channels_impl.domain.model.StreamType
import ru.snowadv.events_api.domain.model.EventQueueProperties

internal sealed interface StreamListCommandElm {
    data class LoadStreams(val type: StreamType) : StreamListCommandElm
    data class LoadTopics(val streamId: Long) : StreamListCommandElm
    data class ObserveEvents(
        val isRestart: Boolean,
        val queueProps: EventQueueProperties?,
    ) : StreamListCommandElm
    data class GoToChat(val streamName: String, val topicName: String) : StreamListCommandElm


}