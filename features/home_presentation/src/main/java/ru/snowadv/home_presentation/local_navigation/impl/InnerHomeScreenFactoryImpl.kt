package ru.snowadv.home_presentation.local_navigation.impl

import androidx.fragment.app.Fragment
import dagger.Reusable
import ru.snowadv.channels_presentation.api.ChannelsScreenFactory
import ru.snowadv.home_presentation.local_navigation.InnerHomeScreenFactory
import ru.snowadv.home_presentation.model.InnerHomeScreen
import ru.snowadv.people_presentation.api.PeopleScreenFactory
import ru.snowadv.profile_presentation.api.ProfileScreenFactory
import javax.inject.Inject

@Reusable
class InnerHomeScreenFactoryImpl @Inject constructor(
    private val channelsScreenFactory: ChannelsScreenFactory,
    private val peopleScreenFactory: PeopleScreenFactory,
    private val profileScreenFactory: ProfileScreenFactory,
): InnerHomeScreenFactory {
    override fun createFragment(screen: InnerHomeScreen): Fragment {
        return when(screen) {
            InnerHomeScreen.CHANNELS -> channelsScreenFactory.create()
            InnerHomeScreen.PEOPLE -> peopleScreenFactory.create()
            InnerHomeScreen.PROFILE -> profileScreenFactory.create(null)
        }
    }
}