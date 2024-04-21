package ru.snowadv.profile.di

import ru.snowadv.profile.domain.use_case.GetProfileUseCase
import ru.snowadv.profile.domain.use_case.ListenToPresenceEventsUseCase
import ru.snowadv.profile.presentation.profile.elm.ProfileActorElm
import ru.snowadv.profile.presentation.profile.elm.ProfileStoreFactoryElm

object ProfileGraph {
    internal lateinit var deps: ProfileDeps
    internal val getProfileUseCase by lazy { GetProfileUseCase(deps.repo) }
    internal val listenToPresenceEventsUseCase by lazy { ListenToPresenceEventsUseCase(deps.eventRepo) }

    internal val profileActorElm by lazy { ProfileActorElm(deps.router, getProfileUseCase, listenToPresenceEventsUseCase) }

    fun init(deps: ProfileDeps) {
        this.deps = deps
    }
}