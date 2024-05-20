package ru.snowadv.channels_presentation.stream_list.ui.elm

import ru.snowadv.channels_presentation.stream_list.ui.model.UiStream
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.model.ScreenState

internal data class StreamListStateUiElm(
    val screenState: ScreenState<List<DelegateItem>> = ScreenState.Loading(),
    val searchQuery: String = "",
)