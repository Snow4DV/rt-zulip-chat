package ru.snowadv.channels_presentation.stream_list.presentation.elm

import ru.snowadv.channels_domain_api.model.Stream
import ru.snowadv.channels_domain_api.model.StreamTopics
import ru.snowadv.channels_domain_api.model.StreamUnreadMessages
import ru.snowadv.channels_domain_api.model.StreamType
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.Resource
import ru.snowadv.model.ScreenState
import ru.snowadv.presentation.util.toScreenState

internal data class StreamListStateElm(
    val screenState: ScreenState<List<Stream>> = ScreenState.Loading(),
    val screenUnfilteredDataRes: Resource<List<Stream>> = Resource.Loading(),
    val topics: StreamTopics? = null,
    val streamsUnreadMessages: List<StreamUnreadMessages> = emptyList(),
    val searchQuery: String = "",
    val resumed: Boolean = false,
    val eventQueueData: EventQueueProperties?,
    val streamType: StreamType,
) {
    val selectedStream: Stream? = screenState.data
        ?.firstOrNull { it.id == topics?.streamId }
}
