package ru.snowadv.auth_data.di.holder

import ru.snowadv.auth_data.di.dagger.ChatDataComponent
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object ChatDataComponentHolder :
    ComponentHolder<ChatDataAPI, ChatDataDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<ChatDataAPI, ChatDataDependencies, ChatDataComponent>(
        isSingleton = true,
        componentFactory = { deps -> ChatDataComponent.initAndGet(deps) },
    )
    override var dependencyProvider: (() -> ChatDataDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): ChatDataComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): ChatDataAPI = componentHolderDelegate.get()
}