package ru.snowadv.channels_impl.presentation.stream_list.elm

internal sealed interface StreamListEffectElm {
    class ShowInternetErrorWithRetry(val retryEvent: StreamListEventElm): StreamListEffectElm
}