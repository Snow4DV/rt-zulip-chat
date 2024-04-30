package ru.snowadv.channels_impl.presentation.stream_list.elm

import ru.snowadv.channels_api.domain.model.StreamType
import ru.snowadv.event_api.helper.EventQueueProperties

internal sealed interface StreamListCommandElm {
    data class LoadStreams(val type: ru.snowadv.channels_api.domain.model.StreamType) : StreamListCommandElm
    data class LoadTopics(val streamId: Long) : StreamListCommandElm
    data class ObserveEvents(
        val isRestart: Boolean,
        val queueProps: EventQueueProperties?,
    ) : StreamListCommandElm
    data class GoToChat(val streamName: String, val topicName: String) : StreamListCommandElm


}