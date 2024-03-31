package ru.snowadv.home.presentation.stream_list.event

sealed class StreamListEvent {
    class ClickedOnStream(val streamId: Long): StreamListEvent()
    class ClickedOnTopic(val streamId: Long, val topicName: String): StreamListEvent()
    data object ClickedOnReload: StreamListEvent()
}