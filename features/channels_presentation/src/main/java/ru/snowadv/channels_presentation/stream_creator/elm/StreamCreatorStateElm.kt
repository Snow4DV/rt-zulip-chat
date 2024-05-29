package ru.snowadv.channels_presentation.stream_creator.elm

import ru.snowadv.model.ScreenState

internal data class StreamCreatorStateElm(
    val creatingStream: Boolean = false,
    val streamName: String = "",
    val description: String = "",
    val announce: Boolean = true,
    val isHistoryAvailableToNewSubs: Boolean = true,
)