package ru.snowadv.home_presentation.home.elm

import dagger.Reusable
import ru.snowadv.home_presentation.home.elm.HomeEffectElm
import ru.snowadv.home_presentation.home.elm.HomeEventElm
import ru.snowadv.home_presentation.home.elm.HomeActorElm
import ru.snowadv.home_presentation.home.elm.HomeReducerElm
import ru.snowadv.home_presentation.home.elm.HomeStateElm
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject
import javax.inject.Provider

@Reusable
internal class HomeStoreFactoryElm @Inject constructor(
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