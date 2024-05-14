package ru.snowadv.channels.presentation.stream_list.elm

import ru.snowadv.channels.domain.model.StreamType
import ru.snowadv.channels.presentation.channel_list.elm.ChannelListActorElm
import ru.snowadv.channels.presentation.channel_list.elm.ChannelListEffectElm
import ru.snowadv.channels.presentation.channel_list.elm.ChannelListEventElm
import ru.snowadv.channels.presentation.channel_list.elm.ChannelListReducerElm
import ru.snowadv.channels.presentation.channel_list.elm.ChannelListStateElm
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

internal class StreamListStoreFactoryElm(
    private val actor: StreamListActorElm,
    private val streamType: StreamType,
) {

    fun create(): Store<StreamListEventElm, StreamListEffectElm, StreamListStateElm> {
        return ElmStore(
            initialState = StreamListStateElm(searchQuery = "", eventQueueData = null, streamType = streamType),
            actor = actor,
            reducer = StreamListReducerElm(),
            startEvent = StreamListEventElm.Ui.Init,
        )
    }
}
