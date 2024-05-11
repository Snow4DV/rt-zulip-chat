package ru.snowadv.auth_domain_impl.di

import dagger.Component
import ru.snowadv.auth_domain_api.di.AuthDomainAPI
import ru.snowadv.auth_domain_api.di.AuthDomainDependencies
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [AuthDomainDependencies::class], modules = [AuthDomainModule::class])
@PerScreen
internal interface AuthDomainComponent : AuthDomainAPI {
    companion object {
        fun initAndGet(deps: AuthDomainDependencies): AuthDomainComponent {
            return DaggerAuthDomainComponent.builder()
                .authDomainDependencies(deps)
                .build()
        }
    }
}