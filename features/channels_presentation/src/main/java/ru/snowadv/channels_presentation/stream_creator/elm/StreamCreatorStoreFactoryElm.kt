package ru.snowadv.channels_presentation.stream_creator.elm

import dagger.Reusable
import ru.snowadv.model.ScreenState
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject
import javax.inject.Provider

@Reusable
internal class StreamCreatorStoreFactoryElm @Inject constructor(
    private val actor: Actor<StreamCreatorCommandElm, StreamCreatorEventElm>,
    private val reducer: Provider<ScreenDslReducer<StreamCreatorEventElm, StreamCreatorEventElm.Ui, StreamCreatorEventElm.Internal, StreamCreatorStateElm, StreamCreatorEffectElm, StreamCreatorCommandElm>>,
) {

    fun create(): Store<StreamCreatorEventElm, StreamCreatorEffectElm, StreamCreatorStateElm> {
        return ElmStore(
            initialState = StreamCreatorStateElm(),
            actor = actor,
            reducer = reducer.get(),
        )
    }
}