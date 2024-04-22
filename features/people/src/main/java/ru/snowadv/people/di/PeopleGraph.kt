package ru.snowadv.people.di

import ru.snowadv.people.domain.use_case.GetPeopleUseCase
import ru.snowadv.people.domain.use_case.ListenToPresenceEventsUseCase
import ru.snowadv.people.presentation.people_list.elm.PeopleListActorElm

object PeopleGraph {
    internal lateinit var deps: PeopleDeps
    internal val getPeopleUseCase by lazy { GetPeopleUseCase(deps.peopleRepository, deps.defaultDispatcher) }
    internal val listenToPresenceEventsUseCase by lazy { ListenToPresenceEventsUseCase(deps.eventRepo) }
    internal val peopleListActorElm by lazy { PeopleListActorElm(getPeopleUseCase, listenToPresenceEventsUseCase, deps.router) }
    fun init(deps: PeopleDeps) {
        this.deps = deps
    }
}