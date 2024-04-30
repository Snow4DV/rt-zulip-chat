package ru.snowadv.voiceapp.di.legacy

import com.github.terrakok.cicerone.Router
import ru.snowadv.channels.di.ChannelsGraph
import ru.snowadv.chat_impl.di.ChatGraph
import di.HomeGraph
import ru.snowadv.people.di.PeopleGraph
import ru.snowadv.voiceapp.di.legacy.deps.ChannelsDepsProvider
import ru.snowadv.voiceapp.di.legacy.deps.ChatDepsProvider
import ru.snowadv.voiceapp.di.legacy.deps.HomeDepsProvider
import ru.snowadv.voiceapp.di.legacy.deps.MainDepsProvider
import ru.snowadv.voiceapp.di.legacy.deps.PeopleDepsProvider

internal object MainGraph { // Will be replaced with proper DI

    lateinit var mainDepsProvider: MainDepsProvider

    private val channelsDepsProvider by lazy { ChannelsDepsProvider() }
    private val chatDepsProvider by lazy { ChatDepsProvider() }
    private val peopleDepsProvider by lazy { PeopleDepsProvider() }
    private val homeDepsProvider by lazy { HomeDepsProvider() }


    fun init(router: Router) {
        mainDepsProvider = MainDepsProvider(router)
        initFeatureGraphs()
    }

    private fun initFeatureGraphs() {
        ChannelsGraph.init(channelsDepsProvider)
        ru.snowadv.chat_impl.di.ChatGraph.init(chatDepsProvider)
        PeopleGraph.init(peopleDepsProvider)
        di.HomeGraph.init(homeDepsProvider)
    }
}