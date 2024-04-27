package ru.snowadv.voiceapp.glue.navigation

import androidx.fragment.app.Fragment
import dagger.Reusable
import ru.snowadv.channels.presentation.feature.ChannelsFeatureScreens
import ru.snowadv.home.presentation.local_navigation.HomeScreen
import ru.snowadv.home.presentation.local_navigation.HomeScreenFactory
import ru.snowadv.people.presentation.feature.PeopleFeatureScreens
import ru.snowadv.profile.presentation.feature.ProfileFeatureScreens
import ru.snowadv.voiceapp.navigation.Screens
import javax.inject.Inject

@Reusable
class HomeScreenFactoryImpl @Inject constructor(): HomeScreenFactory {
    override fun createFragment(screen: HomeScreen): Fragment {
        return when(screen) {
            HomeScreen.CHANNELS -> ChannelsFeatureScreens.Channels()
            HomeScreen.PEOPLE -> PeopleFeatureScreens.People()
            HomeScreen.PROFILE -> ProfileFeatureScreens.Profile()
        }
    }
}