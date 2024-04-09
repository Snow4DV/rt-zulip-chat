package ru.snowadv.home.di

object HomeGraph {
    internal lateinit var deps: HomeDeps
    fun init(deps: HomeDeps) {
        this.deps = deps
    }
}