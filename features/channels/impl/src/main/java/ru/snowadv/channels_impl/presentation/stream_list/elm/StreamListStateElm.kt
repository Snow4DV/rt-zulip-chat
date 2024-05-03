package ru.snowadv.channels_impl.presentation.stream_list.elm

import ru.snowadv.channels_impl.domain.model.StreamType
import ru.snowadv.channels_impl.presentation.model.Stream
import ru.snowadv.channels_impl.presentation.model.StreamUnreadMessages
import ru.snowadv.event_api.helper.EventQueueProperties
import ru.snowadv.model.Resource
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.model.ScreenState

internal data class StreamListStateElm(
    val screenState: ScreenState<List<DelegateItem>> = ScreenState.Loading,
    val screenUnfilteredDataRes: Resource<List<DelegateItem>> = Resource.Loading,
    val streamsUnreadMessages: List<StreamUnreadMessages> = emptyList(),
    val searchQuery: String = "",
    val resumed: Boolean = false,
    val eventQueueData: EventQueueProperties?,
    val streamType: StreamType,
) {
    val selectedStream: Stream? = (screenState as? ScreenState.Success<List<DelegateItem>>)
        ?.data?.asSequence()
        ?.filterIsInstance<Stream>()
        ?.firstOrNull { it.expanded }
}