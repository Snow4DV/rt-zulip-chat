package ru.snowadv.channels.presentation.channel_list.state

import ru.snowadv.channels.domain.model.StreamType

internal data class ChannelListScreenState(
    val searchQuery: String = "",
    val streamTypes: List<StreamType> = StreamType.entries
)