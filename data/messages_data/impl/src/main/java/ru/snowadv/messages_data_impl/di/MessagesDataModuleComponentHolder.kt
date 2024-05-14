package ru.snowadv.messages_data_impl.di

import ru.snowadv.messages_data_api.di.MessagesDataModuleAPI
import ru.snowadv.messages_data_api.di.MessagesDataModuleDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object MessagesDataModuleComponentHolder :
    ComponentHolder<MessagesDataModuleAPI, MessagesDataModuleDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<MessagesDataModuleAPI, MessagesDataModuleDependencies, MessagesDataModuleComponent> {
            deps -> MessagesDataModuleComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> MessagesDataModuleDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): MessagesDataModuleComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): MessagesDataModuleAPI = componentHolderDelegate.get()
}