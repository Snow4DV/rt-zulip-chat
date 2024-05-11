package ru.snowadv.chat_presentation.di.holder

import ru.snowadv.chat_presentation.di.dagger.ChatPresentationComponent
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object ChatPresentationComponentHolder :
    ComponentHolder<ChatPresentationAPI, ChatPresentationDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<ChatPresentationAPI, ChatPresentationDependencies, ChatPresentationComponent> {
            deps -> ChatPresentationComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> ChatPresentationDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): ChatPresentationComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): ChatPresentationAPI = componentHolderDelegate.get()
}