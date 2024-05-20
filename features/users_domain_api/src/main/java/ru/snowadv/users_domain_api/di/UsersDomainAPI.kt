package ru.snowadv.users_domain_api.di

import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.users_domain_api.use_case.GetPeopleUseCase
import ru.snowadv.users_domain_api.use_case.GetProfileUseCase
import ru.snowadv.users_domain_api.use_case.ListenToPeoplePresenceEventsUseCase
import ru.snowadv.users_domain_api.use_case.ListenToProfilePresenceEventsUseCase

interface UsersDomainAPI: BaseModuleAPI {
    val getPeopleUseCase: GetPeopleUseCase
    val getProfileUseCase: GetProfileUseCase

    val listenToPeoplePresenceEventsUseCase: ListenToPeoplePresenceEventsUseCase
    val listenToProfilePresenceEventsUseCase: ListenToProfilePresenceEventsUseCase
}