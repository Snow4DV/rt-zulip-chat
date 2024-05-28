package ru.snowadv.chat_domain_impl.di

import ru.snowadv.chat_domain_api.di.ChatDomainAPI
import ru.snowadv.chat_domain_api.di.ChatDomainDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object ChatDomainComponentHolder :
    ComponentHolder<ChatDomainAPI, ChatDomainDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<ChatDomainAPI, ChatDomainDependencies, ChatDomainComponent>(
        componentFactory = { deps -> ChatDomainComponent.initAndGet(deps) },
    )
    override var dependencyProvider: (() -> ChatDomainDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): ChatDomainComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): ChatDomainAPI = componentHolderDelegate.get()
}