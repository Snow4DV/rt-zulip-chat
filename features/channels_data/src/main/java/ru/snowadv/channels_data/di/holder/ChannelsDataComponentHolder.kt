package ru.snowadv.channels_data.di.holder

import ru.snowadv.channels_data.di.dagger.ChannelsDataComponent
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object ChannelsDataComponentHolder :
    ComponentHolder<ChannelsDataAPI, ChannelsDataDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<ChannelsDataAPI, ChannelsDataDependencies, ChannelsDataComponent>(
        componentFactory = { deps -> ChannelsDataComponent.initAndGet(deps) },
    )
    override var dependencyProvider: (() -> ChannelsDataDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): ChannelsDataComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): ChannelsDataAPI = componentHolderDelegate.get()
}