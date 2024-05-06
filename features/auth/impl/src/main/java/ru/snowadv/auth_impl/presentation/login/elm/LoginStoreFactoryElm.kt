package ru.snowadv.auth_impl.presentation.login.elm

import dagger.Reusable
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject
import javax.inject.Provider

@Reusable
internal class LoginStoreFactoryElm @Inject constructor(
    private val actor: Actor<LoginCommandElm, LoginEventElm>,
    private val reducer: Provider<ScreenDslReducer<LoginEventElm, LoginEventElm.Ui, LoginEventElm.Internal, LoginStateElm, LoginEffectElm, LoginCommandElm>>,
) {

    fun create(): Store<LoginEventElm, LoginEffectElm, LoginStateElm> {
        return ElmStore(
            initialState = LoginStateElm(
                loading = false,
                email = "",
                password = "",
            ),
            actor = actor,
            reducer = reducer.get(),
            startEvent = LoginEventElm.Ui.Init,
        )
    }
}