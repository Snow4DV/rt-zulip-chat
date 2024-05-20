package ru.snowadv.channels_presentation.stream_list.ui.elm

import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListEventElm

internal sealed interface StreamListEffectUiElm {
    data class ShowInternetErrorWithRetry(val retryEvent: StreamListEventElm): StreamListEffectUiElm
}
