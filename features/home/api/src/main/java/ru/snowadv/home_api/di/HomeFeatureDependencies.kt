package ru.snowadv.home_api.di

import ru.snowadv.channels_api.presentation.ChannelsScreenFactory
import ru.snowadv.home_api.presentation.local_navigation.InnerHomeScreenFactory
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.people_api.presentation.PeopleScreenFactory
import ru.snowadv.profile_api.presentation.ProfileScreenFactory

interface HomeFeatureDependencies : BaseModuleDependencies {
    val channelsScreenFactory: ChannelsScreenFactory
    val peopleScreenFactory: PeopleScreenFactory
    val profileScreenFactory: ProfileScreenFactory
}