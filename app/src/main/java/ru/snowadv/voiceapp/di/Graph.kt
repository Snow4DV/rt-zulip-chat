package ru.snowadv.voiceapp.di

import com.github.terrakok.cicerone.Router
import ru.snowadv.chat.di.ChatGraph
import ru.snowadv.chat.presentation.navigation.ChatRouter
import ru.snowadv.home.presentation.di.HomeGraph
import ru.snowadv.home.presentation.navigation.HomeRouter
import ru.snowadv.voiceapp.navigation.impl.ChatRouterImpl
import ru.snowadv.voiceapp.navigation.impl.HomeRouterImpl

object Graph { // Will be replaced with proper DI
    private lateinit var chatRouter: ChatRouter
    private lateinit var homeRouter: HomeRouter

    fun init(router: Router) {
        chatRouter = ChatRouterImpl(router)
        homeRouter = HomeRouterImpl(router)
        initFeatureGraphs()
    }

    private fun initFeatureGraphs() {
        ChatGraph.init(chatRouter)
        HomeGraph.init(homeRouter)
    }
}