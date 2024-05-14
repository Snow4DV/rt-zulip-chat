package ru.snowadv.channels_impl.presentation.channel_list.elm

sealed interface ChannelListEffectElm {
    data object ShowKeyboardAndFocusOnTextField: ChannelListEffectElm
}