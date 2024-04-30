package ru.snowadv.voiceapp.glue.navigation

import androidx.fragment.app.Fragment
import dagger.Reusable
import ru.snowadv.channels.presentation.feature.ChannelsFeatureScreens
import ru.snowadv.home_api.presentation.local_navigation.HomeScreen
import ru.snowadv.home_api.presentation.local_navigation.HomeScreenFactory
import ru.snowadv.people_impl.presentation.feature.PeopleFeatureScreens
import ru.snowadv.profile.presentation.feature.ProfileFeatureScreens
import ru.snowadv.voiceapp.navigation.Screens
import javax.inject.Inject

@Reusable
class HomeScreenFactoryImpl @Inject constructor():
    ru.snowadv.home_api.presentation.local_navigation.HomeScreenFactory {
    override fun createFragment(screen: ru.snowadv.home_api.presentation.local_navigation.HomeScreen): Fragment {
        return when(screen) {
            ru.snowadv.home_api.presentation.local_navigation.HomeScreen.CHANNELS -> ChannelsFeatureScreens.Channels()
            ru.snowadv.home_api.presentation.local_navigation.HomeScreen.PEOPLE -> ru.snowadv.people_impl.presentation.feature.PeopleFeatureScreens.People()
            ru.snowadv.home_api.presentation.local_navigation.HomeScreen.PROFILE -> ProfileFeatureScreens.Profile()
        }
    }
}