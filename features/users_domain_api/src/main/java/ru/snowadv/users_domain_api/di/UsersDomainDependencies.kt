package ru.snowadv.users_domain_api.di

import ru.snowadv.events_api.repository.EventRepository
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.users_domain_api.repository.UserRepository

interface UsersDomainDependencies: BaseModuleDependencies {
    val profileRepository: UserRepository
    val eventRepository: EventRepository
    val dispatcherProvider: DispatcherProvider
}