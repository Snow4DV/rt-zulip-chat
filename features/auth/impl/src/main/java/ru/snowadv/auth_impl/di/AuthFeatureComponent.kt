package ru.snowadv.auth_impl.di

import dagger.Component
import ru.snowadv.auth_api.di.AuthFeatureAPI
import ru.snowadv.auth_api.di.AuthFeatureDependencies
import ru.snowadv.auth_impl.presentation.login.LoginFragment
import ru.snowadv.auth_impl.presentation.login.elm.LoginStoreFactoryElm
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [AuthFeatureDependencies::class], modules = [AuthFeatureModule::class])
@PerScreen
internal interface AuthFeatureComponent : AuthFeatureAPI {
    val loginStoreFactory: LoginStoreFactoryElm
    companion object {
        fun initAndGet(deps: AuthFeatureDependencies): AuthFeatureComponent {
            return DaggerAuthFeatureComponent.builder()
                .authFeatureDependencies(deps)
                .build()
        }
    }
}