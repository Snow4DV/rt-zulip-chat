package ru.snowadv.auth_presentation.di.dagger

import dagger.Component
import ru.snowadv.auth_presentation.di.holder.AuthPresentationAPI
import ru.snowadv.auth_presentation.di.holder.AuthPresentationDependencies
import ru.snowadv.auth_presentation.login.elm.LoginStoreFactoryElm
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [AuthPresentationDependencies::class], modules = [AuthPresentationModule::class])
@PerScreen
internal interface AuthPresentationComponent : AuthPresentationAPI {
    val storeFactory: LoginStoreFactoryElm
    companion object {
        fun initAndGet(deps: AuthPresentationDependencies): AuthPresentationComponent {
            return DaggerAuthPresentationComponent.builder()
                .authPresentationDependencies(deps)
                .build()
        }
    }
}