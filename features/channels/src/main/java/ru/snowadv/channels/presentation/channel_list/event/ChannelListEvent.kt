package ru.snowadv.channels.presentation.channel_list.event

sealed class ChannelListEvent {
    data object SearchIconClicked: ChannelListEvent()
}