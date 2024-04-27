package ru.snowadv.profile.di.holder

import ru.snowadv.event_api.repository.EventRepository
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.profile.domain.navigation.ProfileRouter
import ru.snowadv.profile.domain.repository.ProfileRepository

interface ProfileFeatureDependencies : BaseModuleDependencies {
    val router: ProfileRouter
    val repo: ProfileRepository
    val eventRepo: EventRepository
}