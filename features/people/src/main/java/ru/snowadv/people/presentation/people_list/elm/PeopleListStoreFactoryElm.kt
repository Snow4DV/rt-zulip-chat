package ru.snowadv.people.presentation.people_list.elm

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

internal class PeopleListStoreFactoryElm(
    private val actor: PeopleListActorElm,
) {

    fun create(): Store<PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm> {
        return ElmStore(
            initialState = PeopleListStateElm(eventQueueData = null, isResumed = false, searchQuery = ""),
            actor = actor,
            reducer = PeopleListReducerElm(),
            startEvent = PeopleListEventElm.Ui.Init,
        )
    }
}