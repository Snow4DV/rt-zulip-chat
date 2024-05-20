package ru.snowadv.home_presentation.di.holder

import ru.snowadv.channels_presentation.api.ChannelsScreenFactory
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.people_presentation.api.PeopleScreenFactory
import ru.snowadv.profile_presentation.api.ProfileScreenFactory

interface HomePresentationDependencies : BaseModuleDependencies {
    val channelsScreenFactory: ChannelsScreenFactory
    val peopleScreenFactory: PeopleScreenFactory
    val profileScreenFactory: ProfileScreenFactory
}