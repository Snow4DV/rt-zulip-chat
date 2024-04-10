package ru.snowadv.channels.presentation.channel_list.event

sealed class ChannelListEvent {
    class SearchQueryChanged(val query: String): ChannelListEvent()
    class ClickedOnTopic(val streamName: String, val topicName: String): ChannelListEvent()
}