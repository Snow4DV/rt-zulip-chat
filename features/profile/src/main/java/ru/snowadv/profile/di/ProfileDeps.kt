package ru.snowadv.profile.di

import ru.snowadv.profile.domain.navigation.ProfileRouter
import ru.snowadv.profile.domain.repository.ProfileRepository

interface ProfileDeps {
    val router: ProfileRouter
    val repo: ProfileRepository
}