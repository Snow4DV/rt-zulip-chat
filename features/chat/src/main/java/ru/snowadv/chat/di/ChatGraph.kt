package ru.snowadv.chat.di

import ru.snowadv.chat.domain.navigation.ChatRouter

object ChatGraph {
    internal lateinit var router: ChatRouter

    fun init(router: ChatRouter) {
        this.router = router
    }
}