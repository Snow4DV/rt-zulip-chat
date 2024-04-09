package ru.snowadv.profile.di

object ProfileGraph {
    internal lateinit var deps: ProfileDeps
    fun init(deps: ProfileDeps) {
        this.deps = deps
    }
}