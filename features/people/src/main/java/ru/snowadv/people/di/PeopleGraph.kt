package ru.snowadv.people.di

import ru.snowadv.people.domain.use_case.GetPeopleUseCase
import ru.snowadv.people.domain.use_case.ListenToPresenceEventsUseCase

object PeopleGraph {
    internal lateinit var deps: PeopleDeps
    internal val getPeopleUseCase by lazy { GetPeopleUseCase(deps.peopleRepository) }
    internal val listenToPresenceEventsUseCase by lazy { ListenToPresenceEventsUseCase(deps.eventRepo) }
    fun init(deps: PeopleDeps) {
        this.deps = deps
    }
}