package ru.snowadv.auth_domain_impl.di

import ru.snowadv.auth_domain_api.di.AuthDomainAPI
import ru.snowadv.auth_domain_api.di.AuthDomainDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object AuthDomainComponentHolder :
    ComponentHolder<AuthDomainAPI, AuthDomainDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<AuthDomainAPI, AuthDomainDependencies, AuthDomainComponent>(
        componentFactory = { deps -> AuthDomainComponent.initAndGet(deps) },
    )
    override var dependencyProvider: (() -> AuthDomainDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): AuthDomainComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): AuthDomainAPI = componentHolderDelegate.get()
}