package ru.snowadv.channels.presentation.channel_list.elm

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

internal class ChannelListStoreFactoryElm(
    private val actor: ChannelListActorElm,
) {

    fun create(): Store<ChannelListEventElm, ChannelListEffectElm, ChannelListStateElm> {
        return ElmStore(
            initialState = ChannelListStateElm(searchQuery = ""),
            actor = actor,
            reducer = ChannelListReducerElm(),
        )
    }
}
