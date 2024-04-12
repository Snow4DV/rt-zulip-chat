package ru.snowadv.voiceapp.glue.navigation

import androidx.fragment.app.Fragment
import ru.snowadv.channels.presentation.channel_list.ChannelListFragment
import ru.snowadv.home.presentation.local_navigation.HomeScreen
import ru.snowadv.home.presentation.local_navigation.HomeScreenFactory
import ru.snowadv.people.presentation.people_list.PeopleFragment
import ru.snowadv.profile.presentation.profile.ProfileFragment
import ru.snowadv.voiceapp.navigation.Screens

class HomeScreenFactoryImpl: HomeScreenFactory {
    override fun createFragment(screen: HomeScreen): Fragment {
        return when(screen) {
            HomeScreen.CHANNELS -> ChannelListFragment.newInstance()
            HomeScreen.PEOPLE -> PeopleFragment.newInstance()
            HomeScreen.PROFILE -> ProfileFragment.newInstance() // Will use id from auth repository in future
        }
    }
}