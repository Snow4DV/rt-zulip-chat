package ru.snowadv.channels_impl.presentation.channel_list.elm

import dagger.Reusable
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject
import javax.inject.Provider

@Reusable
internal class ChannelListStoreFactoryElm @Inject constructor(
    private val actor: Actor<ChannelListCommandElm, ChannelListEventElm>,
    private val reducer: Provider<ScreenDslReducer<ChannelListEventElm, ChannelListEventElm.Ui, ChannelListEventElm.Internal, ChannelListStateElm, ChannelListEffectElm, ChannelListCommandElm>>,
) {

    fun create(): Store<ChannelListEventElm, ChannelListEffectElm, ChannelListStateElm> {
        return ElmStore(
            initialState = ChannelListStateElm(searchQuery = ""),
            actor = actor,
            reducer = reducer.get(),
        )
    }
}
