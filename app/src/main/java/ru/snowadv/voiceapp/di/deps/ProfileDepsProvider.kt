package ru.snowadv.voiceapp.di.deps

import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.profile.di.ProfileDeps
import ru.snowadv.profile.domain.navigation.ProfileRouter
import ru.snowadv.profile.domain.repository.ProfileRepository
import ru.snowadv.voiceapp.di.MainGraph
import ru.snowadv.voiceapp.glue.navigation.ProfileRouterImpl
import ru.snowadv.voiceapp.glue.repository.ProfileRepositoryImpl

class ProfileDepsProvider: ProfileDeps {
    override val router: ProfileRouter by lazy { ProfileRouterImpl(MainGraph.mainDepsProvider.router) }
    override val repo: ProfileRepository by lazy { ProfileRepositoryImpl(MainGraph.mainDepsProvider.userDataRepository) }
}