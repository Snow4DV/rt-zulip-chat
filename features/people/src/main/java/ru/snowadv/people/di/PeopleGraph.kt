package ru.snowadv.people.di

object PeopleGraph {
    internal lateinit var deps: PeopleDeps
    fun init(deps: PeopleDeps) {
        this.deps = deps
    }
}