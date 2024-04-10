package ru.snowadv.channels.di

import ru.snowadv.channels.domain.navigation.ChannelsRouter

object ChannelsGraph {
    internal lateinit var router: ChannelsRouter


    fun init(router: ChannelsRouter) {
        this.router = router
    }
}