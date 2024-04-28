package ru.snowadv.events_data_impl.di

import ru.snowadv.events_data_api.di.EventsDataModuleAPI
import ru.snowadv.events_data_api.di.EventsDataModuleDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object EventsDataModuleComponentHolder :
    ComponentHolder<EventsDataModuleAPI, EventsDataModuleDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<EventsDataModuleAPI, EventsDataModuleDependencies, EventsDataModuleComponent> {
            deps -> EventsDataModuleComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> EventsDataModuleDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): EventsDataModuleComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): EventsDataModuleAPI = componentHolderDelegate.get()
}