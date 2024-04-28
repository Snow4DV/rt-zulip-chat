package ru.snowadv.network.di.holder

import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate
import ru.snowadv.network.di.dagger.NetworkModuleComponent

object NetworkModuleComponentHolder : ComponentHolder<NetworkModuleAPI, NetworkModuleDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<NetworkModuleAPI, NetworkModuleDependencies, NetworkModuleComponent> {
        deps -> NetworkModuleComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> NetworkModuleDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): NetworkModuleComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): NetworkModuleAPI = componentHolderDelegate.get()
}