package ru.snowadv.channels.presentation.channel_list.event

sealed class ChannelListEvent {
    class SearchQueryChanged(val query: String): ChannelListEvent()
}