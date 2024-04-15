package ru.snowadv.people.di

import ru.snowadv.people.domain.use_case.GetPeopleUseCase

object PeopleGraph {
    internal lateinit var deps: PeopleDeps
    internal val getPeopleUseCase by lazy { GetPeopleUseCase(deps.peopleRepository) }
    fun init(deps: PeopleDeps) {
        this.deps = deps
    }
}