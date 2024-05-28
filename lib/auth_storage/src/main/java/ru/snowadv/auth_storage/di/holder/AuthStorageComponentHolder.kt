package ru.snowadv.auth_storage.di.holder

import ru.snowadv.auth_storage.di.dagger.AuthStorageComponent
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object AuthStorageComponentHolder :
    ComponentHolder<AuthStorageAPI, AuthStorageDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<AuthStorageAPI, AuthStorageDependencies, AuthStorageComponent>(
        isSingleton = true,
        componentFactory = { deps -> AuthStorageComponent.initAndGet(deps) },
    )
    override var dependencyProvider: (() -> AuthStorageDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): AuthStorageComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): AuthStorageAPI = componentHolderDelegate.get()
}