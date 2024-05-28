package ru.snowadv.channels_domain_impl.di

import ru.snowadv.channels_domain_api.di.ChannelsDomainAPI
import ru.snowadv.channels_domain_api.di.ChannelsDomainDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object ChannelsDomainComponentHolder :
    ComponentHolder<ChannelsDomainAPI, ChannelsDomainDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<ChannelsDomainAPI, ChannelsDomainDependencies, ChannelsDomainComponent>(
        componentFactory = { deps -> ChannelsDomainComponent.initAndGet(deps) },
    )
    override var dependencyProvider: (() -> ChannelsDomainDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): ChannelsDomainComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): ChannelsDomainAPI = componentHolderDelegate.get()
}