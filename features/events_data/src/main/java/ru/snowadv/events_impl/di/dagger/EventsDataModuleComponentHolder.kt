package ru.snowadv.events_impl.di.dagger

import ru.snowadv.events_impl.di.holder.EventsDataAPI
import ru.snowadv.events_impl.di.holder.EventsDataDependencies
import ru.snowadv.events_impl.di.holder.EventsDataModuleComponent
import ru.snowadv.module_injector.component_holder.ComponentHolder
import ru.snowadv.module_injector.component_holder.impl.ComponentHolderDelegate

object EventsDataModuleComponentHolder :
    ComponentHolder<EventsDataAPI, EventsDataDependencies> {

    private val componentHolderDelegate = ComponentHolderDelegate<EventsDataAPI, EventsDataDependencies, EventsDataModuleComponent> {
            deps -> EventsDataModuleComponent.initAndGet(deps)
    }
    override var dependencyProvider: (() -> EventsDataDependencies)? by componentHolderDelegate::dependencyProvider
    internal fun getComponent(): EventsDataModuleComponent = componentHolderDelegate.getComponentImpl()
    override fun get(): EventsDataAPI = componentHolderDelegate.get()
}