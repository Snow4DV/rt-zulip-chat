package ru.snowadv.profile.di

import ru.snowadv.profile.domain.navigation.ProfileRouter

object ProfileGraph {
    internal lateinit var router: ProfileRouter


    fun init(router: ProfileRouter) {
        this.router = router
    }
}