package ru.snowadv.profile.di

import ru.snowadv.event_api.repository.EventRepository
import ru.snowadv.profile.domain.navigation.ProfileRouter
import ru.snowadv.profile.domain.repository.ProfileRepository

interface ProfileDeps {
    val router: ProfileRouter
    val repo: ProfileRepository
    val eventRepo: EventRepository
}