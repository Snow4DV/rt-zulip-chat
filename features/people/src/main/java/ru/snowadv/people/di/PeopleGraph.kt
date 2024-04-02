package ru.snowadv.people.di

import ru.snowadv.people.domain.navigation.PeopleRouter

object PeopleGraph {
    internal lateinit var router: PeopleRouter


    fun init(router: PeopleRouter) {
        this.router = router
    }
}