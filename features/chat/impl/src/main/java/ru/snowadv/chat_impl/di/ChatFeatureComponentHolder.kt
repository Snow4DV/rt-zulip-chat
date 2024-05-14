package ru.snowadv.chat_impl.di

import ru.snowadv.chat_api.di.ChatFeatureAPI
import ru.snowadv.chat_api.di.ChatFeatureDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate
object ChatFeatureComponentHolder :
    ComponentHolder<ChatFeatureAPI, ChatFeatureDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<ChatFeatureAPI, ChatFeatureDependencies, ChatFeatureComponent> {
            deps -> ChatFeatureComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> ChatFeatureDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): ChatFeatureComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): ChatFeatureAPI = componentHolderDelegate.get()
}