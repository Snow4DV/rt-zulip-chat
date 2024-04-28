package ru.snowadv.events_data_impl.di

import dagger.Component
import ru.snowadv.events_data_api.di.EventsDataModuleAPI
import ru.snowadv.events_data_api.di.EventsDataModuleDependencies
import javax.inject.Singleton

@Singleton
@Component(dependencies = [EventsDataModuleDependencies::class], modules = [EventsDataModule::class])
internal interface EventsDataModuleComponent : EventsDataModuleAPI {
    companion object {
        fun initAndGet(deps: EventsDataModuleDependencies): EventsDataModuleComponent {
            return DaggerEventsDataModuleComponent.builder()
                .eventsDataModuleDependencies(deps)
                .build()
        }
    }
}