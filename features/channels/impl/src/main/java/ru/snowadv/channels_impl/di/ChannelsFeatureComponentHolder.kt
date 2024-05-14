package ru.snowadv.channels_impl.di

import ru.snowadv.channels_api.di.ChannelsFeatureAPI
import ru.snowadv.channels_api.di.ChannelsFeatureDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object ChannelsFeatureComponentHolder :
    ComponentHolder<ChannelsFeatureAPI, ChannelsFeatureDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<ChannelsFeatureAPI, ChannelsFeatureDependencies, ChannelsFeatureComponent> {
            deps -> ChannelsFeatureComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> ChannelsFeatureDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): ChannelsFeatureComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): ChannelsFeatureAPI = componentHolderDelegate.get()
}