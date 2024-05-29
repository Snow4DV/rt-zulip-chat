package ru.snowadv.message_actions_presentation.di.dagger

import ru.snowadv.message_actions_presentation.di.holder.MessageActionsPresentationAPI
import ru.snowadv.message_actions_presentation.di.holder.MessageActionsPresentationDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object MessageActionsPresentationComponentHolder :
    ComponentHolder<MessageActionsPresentationAPI, MessageActionsPresentationDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<MessageActionsPresentationAPI, MessageActionsPresentationDependencies, MessageActionsPresentationComponent> {
            deps -> MessageActionsPresentationComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> MessageActionsPresentationDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): MessageActionsPresentationComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): MessageActionsPresentationAPI = componentHolderDelegate.get()
}