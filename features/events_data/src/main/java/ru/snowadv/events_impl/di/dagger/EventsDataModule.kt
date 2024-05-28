package ru.snowadv.events_impl.di.dagger

import dagger.Binds
import dagger.Module
import ru.snowadv.events_api.repository.EventRepository
import ru.snowadv.events_impl.repository.EventRepositoryImpl

@Module
internal interface EventsDataModule {
    @Binds
    fun bindEventRepositoryImpl(eventDataRepositoryImpl: EventRepositoryImpl): EventRepository
}