package ru.snowadv.users_domain_impl.di

import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate
import ru.snowadv.users_domain_api.di.UsersDomainAPI
import ru.snowadv.users_domain_api.di.UsersDomainDependencies

object UsersDomainComponentHolder :
    ComponentHolder<UsersDomainAPI, UsersDomainDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<UsersDomainAPI, UsersDomainDependencies, UsersDomainComponent>(
        componentFactory = { deps -> UsersDomainComponent.initAndGet(deps) },
    )
    override var dependencyProvider: (() -> UsersDomainDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): UsersDomainComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): UsersDomainAPI = componentHolderDelegate.get()
}