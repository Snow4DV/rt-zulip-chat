package ru.snowadv.channels.presentation.stream_list.elm

internal sealed interface StreamListEffectElm {
    class ShowInternetErrorWithRetry(val retryEvent: StreamListEventElm): StreamListEffectElm
}