package ru.snowadv.properties_provider_impl.di

import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate
import ru.snowadv.properties_provider_api.di.PropertiesProviderModuleAPI
import ru.snowadv.properties_provider_api.di.PropertiesProviderModuleDependencies

object PropertiesProviderModuleComponentHolder :
    ComponentHolder<PropertiesProviderModuleAPI, PropertiesProviderModuleDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<PropertiesProviderModuleAPI, PropertiesProviderModuleDependencies, PropertiesProviderModuleComponent> {
            deps -> PropertiesProviderModuleComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> PropertiesProviderModuleDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): PropertiesProviderModuleComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): PropertiesProviderModuleAPI = componentHolderDelegate.get()
}