package ru.snowadv.people_api.di

import ru.snowadv.events_api.domain.EventRepository
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.people_api.domain.navigation.PeopleRouter
import ru.snowadv.users_data_api.UserDataRepository

interface PeopleFeatureDependencies : BaseModuleDependencies {
    val router: PeopleRouter
    val userDataRepo: UserDataRepository
    val eventRepo: EventRepository
    val dispatcherProvider: DispatcherProvider
}