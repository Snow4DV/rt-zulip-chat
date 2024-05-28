package ru.snowadv.events_impl.di.holder

import dagger.Component
import ru.snowadv.events_impl.di.dagger.EventsDataModule
import javax.inject.Singleton

@Singleton
@Component(dependencies = [EventsDataDependencies::class], modules = [EventsDataModule::class])
internal interface EventsDataModuleComponent : EventsDataAPI {
    companion object {
        fun initAndGet(deps: EventsDataDependencies): EventsDataModuleComponent {
            return DaggerEventsDataModuleComponent.builder()
                .eventsDataDependencies(deps)
                .build()
        }
    }
}