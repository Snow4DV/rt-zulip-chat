package ru.snowadv.home.presentation.channel_list.state

import ru.snowadv.home.domain.model.StreamType

internal data class ChannelListScreenState(
    val searchQuery: String = "",
    val streamTypes: List<StreamType> = StreamType.entries
)