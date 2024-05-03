package ru.snowadv.events_impl.di

import ru.snowadv.events_api.di.EventsFeatureModuleAPI
import ru.snowadv.events_api.di.EventsFeatureModuleDependencies
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object EventsDataModuleComponentHolder :
    ComponentHolder<EventsFeatureModuleAPI, EventsFeatureModuleDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<EventsFeatureModuleAPI, EventsFeatureModuleDependencies, EventsDataModuleComponent> {
            deps -> EventsDataModuleComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> EventsFeatureModuleDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): EventsDataModuleComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): EventsFeatureModuleAPI = componentHolderDelegate.get()
}