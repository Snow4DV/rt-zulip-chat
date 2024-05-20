package ru.snowadv.channels_presentation.stream_list.presentation.elm

internal sealed interface StreamListEffectElm {
    class ShowInternetErrorWithRetry(val retryEvent: StreamListEventElm): StreamListEffectElm
}