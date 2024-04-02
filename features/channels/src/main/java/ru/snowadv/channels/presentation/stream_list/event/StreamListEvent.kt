package ru.snowadv.channels.presentation.stream_list.event

sealed class StreamListEvent {
    class ClickedOnStream(val streamId: Long): StreamListEvent()
    class ClickedOnTopic(val topicName: String): StreamListEvent()
    data object ClickedOnReload: StreamListEvent()
}