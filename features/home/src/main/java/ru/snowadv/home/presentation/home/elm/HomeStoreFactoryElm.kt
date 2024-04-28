package ru.snowadv.home.presentation.home.elm

import ru.snowadv.home.presentation.home.elm.HomeEffectElm
import ru.snowadv.home.presentation.home.elm.HomeEventElm
import ru.snowadv.home.presentation.home.elm.HomeActorElm
import ru.snowadv.home.presentation.home.elm.HomeReducerElm
import ru.snowadv.home.presentation.home.elm.HomeStateElm
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

internal class HomeStoreFactoryElm(
    private val actor: HomeActorElm,
) {

    fun create(): Store<HomeEventElm, HomeEffectElm, HomeStateElm> {
        return ElmStore(
            initialState = HomeStateElm(),
            actor = actor,
            reducer = HomeReducerElm(),
        )
    }
}