package ru.snowadv.emojis_data_impl.di

import ru.snowadv.emojis_data_api.model.di.EmojisDataModuleAPI
import ru.snowadv.emojis_data_api.model.di.EmojisDataModuleDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object EmojisDataModuleComponentHolder :
    ComponentHolder<EmojisDataModuleAPI, EmojisDataModuleDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<EmojisDataModuleAPI, EmojisDataModuleDependencies, EmojisDataModuleComponent> {
            deps -> EmojisDataModuleComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> EmojisDataModuleDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): EmojisDataModuleComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): EmojisDataModuleAPI = componentHolderDelegate.get()
}