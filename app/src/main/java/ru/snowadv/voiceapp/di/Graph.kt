package ru.snowadv.voiceapp.di

import com.github.terrakok.cicerone.Router
import ru.snowadv.channels.di.ChannelsGraph
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.chat.di.ChatGraph
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.home.di.HomeGraph
import ru.snowadv.home.presentation.local_navigation.HomeScreenFactory
import ru.snowadv.people.di.PeopleGraph
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.profile.di.ProfileGraph
import ru.snowadv.profile.domain.navigation.ProfileRouter
import ru.snowadv.voiceapp.navigation.impl.ChannelsRouterImpl
import ru.snowadv.voiceapp.navigation.impl.ChatRouterImpl
import ru.snowadv.voiceapp.navigation.impl.HomeScreenFactoryImpl
import ru.snowadv.voiceapp.navigation.impl.PeopleRouterImpl
import ru.snowadv.voiceapp.navigation.impl.ProfileRouterImpl

object Graph { // Will be replaced with proper DI
    private lateinit var channelsRouter: ChannelsRouter
    private lateinit var chatRouter: ChatRouter
    private lateinit var peopleRouter: PeopleRouter
    private lateinit var profileRouter: ProfileRouter
    private lateinit var homeScreenFactory: HomeScreenFactory

    fun init(router: Router) {
        channelsRouter = ChannelsRouterImpl(router)
        chatRouter = ChatRouterImpl(router)
        peopleRouter = PeopleRouterImpl(router)
        profileRouter = ProfileRouterImpl(router)
        homeScreenFactory = HomeScreenFactoryImpl()
        initFeatureGraphs()
    }

    private fun initFeatureGraphs() {
        ChannelsGraph.init(channelsRouter)
        ChatGraph.init(chatRouter)
        PeopleGraph.init(peopleRouter)
        ProfileGraph.init(profileRouter)
        HomeGraph.init(homeScreenFactory)
    }
}