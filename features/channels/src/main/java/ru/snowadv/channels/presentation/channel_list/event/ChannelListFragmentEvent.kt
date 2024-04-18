package ru.snowadv.channels.presentation.channel_list.event

sealed class ChannelListFragmentEvent {
    data object ShowKeyboardAndFocusOnTextField: ChannelListFragmentEvent()
}