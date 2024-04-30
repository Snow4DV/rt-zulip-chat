package ru.snowadv.people_api.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.snowadv.events_data_api.EventRepository
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.people_api.domain.navigation.PeopleRouter
import ru.snowadv.people_api.domain.repository.PeopleRepository

interface PeopleFeatureDependencies : BaseModuleDependencies {
    val router: PeopleRouter
    val peopleRepository: PeopleRepository
    val eventRepo: EventRepository
    val dispatcherProvider: DispatcherProvider
}