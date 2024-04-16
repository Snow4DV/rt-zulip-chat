package ru.snowadv.profile.di

import ru.snowadv.profile.domain.use_case.GetProfileUseCase
import ru.snowadv.profile.domain.use_case.ListenToPresenceEventsUseCase

object ProfileGraph {
    internal lateinit var deps: ProfileDeps
    internal val getProfileUseCase by lazy { GetProfileUseCase(deps.repo) }
    internal val listenToPresenceEventsUseCase by lazy { ListenToPresenceEventsUseCase(deps.eventRepo) }
    fun init(deps: ProfileDeps) {
        this.deps = deps
    }
}