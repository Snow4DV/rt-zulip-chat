package ru.snowadv.profile_api.di

import ru.snowadv.events_data_api.EventRepository
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.profile_api.domain.navigation.ProfileRouter
import ru.snowadv.profile_api.domain.repository.ProfileRepository
import ru.snowadv.users_data_api.UserDataRepository

interface ProfileFeatureDependencies : BaseModuleDependencies {
    val router: ProfileRouter
    val userDataRepo: UserDataRepository
    val eventRepo: EventRepository
    val dispatcherProvider: DispatcherProvider
}