package ru.snowadv.home.di

import ru.snowadv.home.presentation.home.elm.HomeActorElm

object HomeGraph {
    internal lateinit var deps: HomeDeps
    internal val homeActorElm by lazy { HomeActorElm() }
    fun init(deps: HomeDeps) {
        this.deps = deps
    }
}