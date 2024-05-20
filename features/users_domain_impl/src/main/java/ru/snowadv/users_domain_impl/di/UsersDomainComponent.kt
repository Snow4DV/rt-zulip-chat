package ru.snowadv.users_domain_impl.di

import dagger.Component
import ru.snowadv.module_injector.general.PerScreen
import ru.snowadv.users_domain_api.di.UsersDomainAPI
import ru.snowadv.users_domain_api.di.UsersDomainDependencies

@Component(dependencies = [UsersDomainDependencies::class], modules = [UsersDomainModule::class])
@PerScreen
internal interface UsersDomainComponent : UsersDomainAPI {
    companion object {
        fun initAndGet(deps: UsersDomainDependencies): UsersDomainComponent {
            return DaggerUsersDomainComponent.builder()
                .usersDomainDependencies(deps)
                .build()
        }
    }
}