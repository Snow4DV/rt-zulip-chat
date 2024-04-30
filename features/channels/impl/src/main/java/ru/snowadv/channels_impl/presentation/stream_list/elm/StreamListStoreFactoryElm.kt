package ru.snowadv.channels_impl.presentation.stream_list.elm

import dagger.Reusable
import ru.snowadv.channels_api.domain.model.StreamType
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject
import javax.inject.Provider

@Reusable
internal class StreamListStoreFactoryElm @Inject constructor(
    private val actor: Actor<StreamListCommandElm, StreamListEventElm>,
    private val reducer: Provider<ScreenDslReducer<StreamListEventElm, StreamListEventElm.Ui, StreamListEventElm.Internal, StreamListStateElm, StreamListEffectElm, StreamListCommandElm>>,
) {

    fun create(streamType: StreamType): Store<StreamListEventElm, StreamListEffectElm, StreamListStateElm> {
        return ElmStore(
            initialState = StreamListStateElm(
                searchQuery = "",
                eventQueueData = null,
                streamType = streamType,
            ),
            actor = actor,
            reducer = reducer.get(),
            startEvent = StreamListEventElm.Ui.Init,
        )
    }
}
