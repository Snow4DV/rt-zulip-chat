package ru.snowadv.events_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.events_api.domain.EventRepository
import ru.snowadv.events_impl.data.EventRepositoryImpl

@Module
internal interface EventsDataModule {
    @Binds
    fun bindEventRepositoryImpl(eventDataRepositoryImpl: EventRepositoryImpl): EventRepository
}