package ru.snowadv.channels_presentation.stream_creator.elm

internal sealed interface StreamCreatorCommandElm {
    data class CreateStream(val name: String, val description: String, val announce: Boolean, val isHistoryAvailableToSubscribers: Boolean) :
        StreamCreatorCommandElm
}