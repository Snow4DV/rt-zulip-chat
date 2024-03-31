package ru.snowadv.home.presentation.di

import ru.snowadv.home.presentation.navigation.HomeRouter

object HomeGraph {
    internal lateinit var router: HomeRouter


    fun init(router: HomeRouter) {
        this.router = router
    }
}