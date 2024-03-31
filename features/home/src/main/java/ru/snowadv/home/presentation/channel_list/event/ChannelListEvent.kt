package ru.snowadv.home.presentation.channel_list.event

import ru.snowadv.home.presentation.stream_list.event.StreamListEvent

sealed class ChannelListEvent {
    class SearchQueryChanged(val query: String): ChannelListEvent()
    class ClickedOnTopic(val streamId: Long, val topicName: String): ChannelListEvent()
}