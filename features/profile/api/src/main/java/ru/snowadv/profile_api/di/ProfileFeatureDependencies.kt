package ru.snowadv.profile_api.di

import ru.snowadv.events_data_api.EventRepository
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.profile_api.domain.navigation.ProfileRouter
import ru.snowadv.profile_api.domain.repository.ProfileRepository

interface ProfileFeatureDependencies : BaseModuleDependencies {
    val router: ProfileRouter
    val repo: ProfileRepository
    val eventRepo: EventRepository
}