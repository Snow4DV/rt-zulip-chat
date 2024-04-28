package ru.snowadv.events_data_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.events_data_api.EventRepository
import ru.snowadv.events_data_impl.EventDataRepositoryImpl

@Module
internal interface EventsDataModule {
    @Binds
    fun bindEventDataRepositoryImpl(eventDataRepositoryImpl: EventDataRepositoryImpl): EventRepository
}