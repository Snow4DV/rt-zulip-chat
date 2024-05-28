package ru.snowadv.channels_presentation.di.holder

import ru.snowadv.channels_presentation.di.dagger.ChannelsPresentationComponent
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object ChannelsPresentationComponentHolder :
    ComponentHolder<ChannelsPresentationAPI, ChannelsPresentationDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<ChannelsPresentationAPI, ChannelsPresentationDependencies, ChannelsPresentationComponent> {
            deps -> ChannelsPresentationComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> ChannelsPresentationDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): ChannelsPresentationComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): ChannelsPresentationAPI = componentHolderDelegate.get()
}