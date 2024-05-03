package ru.snowadv.events_impl.di

import dagger.Component
import ru.snowadv.events_api.di.EventsFeatureModuleAPI
import ru.snowadv.events_api.di.EventsFeatureModuleDependencies
import javax.inject.Singleton

@Singleton
@Component(dependencies = [EventsFeatureModuleDependencies::class], modules = [EventsDataModule::class])
internal interface EventsDataModuleComponent : EventsFeatureModuleAPI {
    companion object {
        fun initAndGet(deps: EventsFeatureModuleDependencies): EventsDataModuleComponent {
            return DaggerEventsDataModuleComponent.builder()
                .eventsFeatureModuleDependencies(deps)
                .build()
        }
    }
}