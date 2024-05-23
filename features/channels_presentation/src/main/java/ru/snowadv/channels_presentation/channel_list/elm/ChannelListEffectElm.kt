package ru.snowadv.channels_presentation.channel_list.elm

sealed interface ChannelListEffectElm {
    data object ShowKeyboardAndFocusOnTextField: ChannelListEffectElm
    data object OpenStreamCreator: ChannelListEffectElm
    data class ShowNewStreamCreated(val name: String): ChannelListEffectElm
}