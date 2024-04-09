package ru.snowadv.voiceapp.di

import com.github.terrakok.cicerone.Router
import ru.snowadv.channels.di.ChannelsGraph
import ru.snowadv.chat.di.ChatGraph
import ru.snowadv.home.di.HomeGraph
import ru.snowadv.people.di.PeopleGraph
import ru.snowadv.profile.di.ProfileGraph
import ru.snowadv.voiceapp.di.deps.ChannelsDepsProvider
import ru.snowadv.voiceapp.di.deps.ChatDepsProvider
import ru.snowadv.voiceapp.di.deps.HomeDepsProvider
import ru.snowadv.voiceapp.di.deps.MainDepsProvider
import ru.snowadv.voiceapp.di.deps.PeopleDepsProvider
import ru.snowadv.voiceapp.di.deps.ProfileDepsProvider

internal object MainGraph { // Will be replaced with proper DI

    val mainDepsProvider by lazy { MainDepsProvider() }
    private val channelsDepsProvider by lazy { ChannelsDepsProvider() }
    private val chatDepsProvider by lazy {ChatDepsProvider()}
    private val peopleDepsProvider by lazy { PeopleDepsProvider() }
    private val profileDepsProvider by lazy { ProfileDepsProvider() }
    private val homeDepsProvider by lazy { HomeDepsProvider() }

    fun init(router: Router) {
        mainDepsProvider.router = router
        initFeatureGraphs()
    }

    private fun initFeatureGraphs() {
        ChannelsGraph.init(channelsDepsProvider)
        ChatGraph.init(chatDepsProvider)
        PeopleGraph.init(peopleDepsProvider)
        ProfileGraph.init(profileDepsProvider)
        HomeGraph.init(homeDepsProvider)
    }
}