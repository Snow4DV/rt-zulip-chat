package ru.snowadv.home_impl.presentation.feature

import androidx.fragment.app.Fragment
import dagger.Reusable
import ru.snowadv.channels_api.presentation.ChannelsScreenFactory
import ru.snowadv.home_api.presentation.local_navigation.InnerHomeScreen
import ru.snowadv.home_api.presentation.local_navigation.InnerHomeScreenFactory
import ru.snowadv.people_api.presentation.PeopleScreenFactory
import ru.snowadv.profile_api.presentation.ProfileScreenFactory
import javax.inject.Inject

@Reusable
class InnerHomeScreenFactoryImpl @Inject constructor(
    private val channelsScreenFactory: ChannelsScreenFactory,
    private val peopleScreenFactory: PeopleScreenFactory,
    private val profileScreenFactory: ProfileScreenFactory,
):
    InnerHomeScreenFactory {
    override fun createFragment(screen: InnerHomeScreen): Fragment {
        return when(screen) {
            InnerHomeScreen.CHANNELS -> channelsScreenFactory.create()
            InnerHomeScreen.PEOPLE -> peopleScreenFactory.create()
            InnerHomeScreen.PROFILE -> profileScreenFactory.create(null)
        }
    }
}