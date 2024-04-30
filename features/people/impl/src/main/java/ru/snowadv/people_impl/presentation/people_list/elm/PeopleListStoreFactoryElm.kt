package ru.snowadv.people_impl.presentation.people_list.elm

import dagger.Reusable
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject
import javax.inject.Provider

@Reusable
internal class PeopleListStoreFactoryElm @Inject constructor(
    private val actor: Actor<PeopleListCommandElm, PeopleListEventElm>,
    private val reducer: Provider<ScreenDslReducer<PeopleListEventElm, PeopleListEventElm.Ui, PeopleListEventElm.Internal, PeopleListStateElm, PeopleListEffectElm, PeopleListCommandElm>>,
) {

    fun create(): Store<PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm> {
        return ElmStore(
            initialState = PeopleListStateElm(eventQueueData = null, isResumed = false, searchQuery = ""),
            actor = actor,
            reducer = reducer.get(),
            startEvent = PeopleListEventElm.Ui.Init,
        )
    }
}