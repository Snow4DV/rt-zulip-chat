package ru.snowadv.auth_data_impl.di

import ru.snowadv.auth_data_api.di.AuthDataModuleAPI
import ru.snowadv.auth_data_api.di.AuthDataModuleDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object AuthDataModuleComponentHolder :
    ComponentHolder<AuthDataModuleAPI, AuthDataModuleDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<AuthDataModuleAPI, AuthDataModuleDependencies, AuthDataModuleComponent>(
        isSingleton = true,
        componentFactory = { deps -> AuthDataModuleComponent.initAndGet(deps) },
    )
    override var dependencyProvider: (() -> AuthDataModuleDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): AuthDataModuleComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): AuthDataModuleAPI = componentHolderDelegate.get()
}