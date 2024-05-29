package ru.snowadv.channels_presentation.stream_creator.elm


internal sealed interface StreamCreatorEffectElm {
    data class CloseWithNewStreamCreated(val newStreamName: String): StreamCreatorEffectElm
    data object ShowInternetError: StreamCreatorEffectElm
}