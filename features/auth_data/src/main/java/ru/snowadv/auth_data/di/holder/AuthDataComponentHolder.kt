package ru.snowadv.auth_data.di.holder

import ru.snowadv.auth_data.di.dagger.AuthDataComponent
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object AuthDataComponentHolder :
    ComponentHolder<AuthDataAPI, AuthDataDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<AuthDataAPI, AuthDataDependencies, AuthDataComponent>(
        componentFactory = { deps -> AuthDataComponent.initAndGet(deps) },
    )
    override var dependencyProvider: (() -> AuthDataDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): AuthDataComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): AuthDataAPI = componentHolderDelegate.get()
}