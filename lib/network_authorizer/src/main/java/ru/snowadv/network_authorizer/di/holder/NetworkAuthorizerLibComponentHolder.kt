package ru.snowadv.network_authorizer.di.holder

import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate
import ru.snowadv.network_authorizer.di.dagger.NetworkAuthorizerLibComponent

object NetworkAuthorizerLibComponentHolder : ComponentHolder<NetworkAuthorizerLibAPI, NetworkAuthorizerLibDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<NetworkAuthorizerLibAPI, NetworkAuthorizerLibDependencies, NetworkAuthorizerLibComponent> {
            deps -> NetworkAuthorizerLibComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> NetworkAuthorizerLibDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): NetworkAuthorizerLibComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): NetworkAuthorizerLibAPI = componentHolderDelegate.get()
}