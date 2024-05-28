package ru.snowadv.auth_presentation.di.dagger

import dagger.Binds
import dagger.Module
import ru.snowadv.auth_presentation.api.AuthScreenFactory
import ru.snowadv.auth_presentation.feature.AuthScreenFactoryImpl
import ru.snowadv.auth_presentation.login.elm.LoginActorElm
import ru.snowadv.auth_presentation.login.elm.LoginCommandElm
import ru.snowadv.auth_presentation.login.elm.LoginEffectElm
import ru.snowadv.auth_presentation.login.elm.LoginEventElm
import ru.snowadv.auth_presentation.login.elm.LoginReducerElm
import ru.snowadv.auth_presentation.login.elm.LoginStateElm
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

@Module
internal interface AuthPresentationModule {
    @Binds
    fun bindLoginActorElm(loginActorElm: LoginActorElm): Actor<LoginCommandElm, LoginEventElm>
    @Binds
    fun bindLoginReducerElm(loginReducerElm: LoginReducerElm): ScreenDslReducer<LoginEventElm, LoginEventElm.Ui, LoginEventElm.Internal, LoginStateElm, LoginEffectElm, LoginCommandElm>
    @Binds
    fun bindAuthScreenFactoryImpl(authScreenFactoryImpl: AuthScreenFactoryImpl): AuthScreenFactory
}
