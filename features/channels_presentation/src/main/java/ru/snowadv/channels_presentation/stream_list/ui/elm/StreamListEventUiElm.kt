package ru.snowadv.channels_presentation.stream_list.ui.elm

import ru.snowadv.channels_presentation.stream_list.ui.model.UiStream

internal sealed interface StreamListEventUiElm {
    data object Init : StreamListEventUiElm
    data object Resumed : StreamListEventUiElm
    data object Paused : StreamListEventUiElm
    data class ClickedOnTopic(val topicName: String) : StreamListEventUiElm
    data class ClickedOnExpandStream(val streamId: Long) : StreamListEventUiElm
    data class ClickedOnOpenStream(val streamId: Long, val streamName: String) : StreamListEventUiElm
    data class ClickedOnChangeStreamSubscriptionStatus(val uiStream: UiStream) : StreamListEventUiElm
    data class ChangedQuery(val query: String) : StreamListEventUiElm
    data object ReloadClicked : StreamListEventUiElm
}