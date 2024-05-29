package ru.snowadv.network.di.holder

import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate
import ru.snowadv.network.di.dagger.NetworkLibComponent

object NetworkLibComponentHolder : ComponentHolder<NetworkLibAPI, NetworkLibDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<NetworkLibAPI, NetworkLibDependencies, NetworkLibComponent>(
        componentFactory = { deps -> NetworkLibComponent.initAndGet(deps) },
    )
    override var dependencyProvider: (() -> NetworkLibDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): NetworkLibComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): NetworkLibAPI = componentHolderDelegate.get()
}