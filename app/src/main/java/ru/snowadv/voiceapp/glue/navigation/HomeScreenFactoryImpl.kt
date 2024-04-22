package ru.snowadv.voiceapp.glue.navigation

import androidx.fragment.app.Fragment
import ru.snowadv.channels.presentation.channel_list.ChannelListFragment
import ru.snowadv.home.presentation.local_navigation.HomeScreen
import ru.snowadv.home.presentation.local_navigation.HomeScreenFactory
import ru.snowadv.people.presentation.feature.PeopleFeatureScreens
import ru.snowadv.profile.presentation.feature.ProfileFeatureScreens
import ru.snowadv.voiceapp.navigation.Screens

class HomeScreenFactoryImpl: HomeScreenFactory {
    override fun createFragment(screen: HomeScreen): Fragment {
        return when(screen) {
            HomeScreen.CHANNELS -> ChannelListFragment.newInstance()
            HomeScreen.PEOPLE -> PeopleFeatureScreens.People()
            HomeScreen.PROFILE -> ProfileFeatureScreens.Profile()
        }
    }
}