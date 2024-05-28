package ru.snowadv.channels_presentation.stream_list.ui.elm

import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListEventElm
import ru.snowadv.model.InputStreamOpener

sealed interface StreamListEventUiElm {
    data object Init : StreamListEventUiElm
    data object Resumed : StreamListEventUiElm
    data object Paused : StreamListEventUiElm
    data class ClickedOnTopic(val topicName: String) : StreamListEventUiElm
    data class ClickedOnStream(val streamId: Long) : StreamListEventUiElm
    data class ChangedQuery(val query: String) : StreamListEventUiElm
    data object ReloadClicked : StreamListEventUiElm
}