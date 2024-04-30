package ru.snowadv.home_impl.presentation.home.elm

import ru.snowadv.home_impl.presentation.home.elm.HomeEffectElm
import ru.snowadv.home_impl.presentation.home.elm.HomeEventElm
import ru.snowadv.home_impl.presentation.home.elm.HomeActorElm
import ru.snowadv.home_impl.presentation.home.elm.HomeReducerElm
import ru.snowadv.home_impl.presentation.home.elm.HomeStateElm
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Provider

internal class HomeStoreFactoryElm(
    private val actor: Actor<HomeCommandElm, HomeEventElm>,
    private val reducer: Provider<ScreenDslReducer<HomeEventElm, HomeEventElm.Ui, HomeEventElm.Internal, HomeStateElm, HomeEffectElm, HomeCommandElm>>,
) {

    fun create(): Store<HomeEventElm, HomeEffectElm, HomeStateElm> {
        return ElmStore(
            initialState = HomeStateElm(),
            actor = actor,
            reducer = reducer.get(),
        )
    }
}