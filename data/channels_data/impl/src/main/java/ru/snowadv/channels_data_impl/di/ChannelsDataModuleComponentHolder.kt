package ru.snowadv.channels_data_impl.di

import ru.snowadv.channels_data_api.di.ChannelsDataModuleAPI
import ru.snowadv.channels_data_api.di.ChannelsDataModuleDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object ChannelsDataModuleComponentHolder :
    ComponentHolder<ChannelsDataModuleAPI, ChannelsDataModuleDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<ChannelsDataModuleAPI, ChannelsDataModuleDependencies, ChannelsDataModuleComponent> {
            deps -> ChannelsDataModuleComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> ChannelsDataModuleDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): ChannelsDataModuleComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): ChannelsDataModuleAPI = componentHolderDelegate.get()
}