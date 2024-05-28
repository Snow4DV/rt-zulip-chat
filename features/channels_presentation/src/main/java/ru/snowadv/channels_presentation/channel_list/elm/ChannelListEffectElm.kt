package ru.snowadv.channels_presentation.channel_list.elm

sealed interface ChannelListEffectElm {
    data object ShowKeyboardAndFocusOnTextField: ChannelListEffectElm
}