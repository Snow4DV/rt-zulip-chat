package ru.snowadv.channels_presentation.di.holder

import ru.snowadv.channels_presentation.api.ChannelsScreenFactory
import ru.snowadv.module_injector.module.BaseModuleAPI

interface ChannelsPresentationAPI : BaseModuleAPI {
    val screenFactory: ChannelsScreenFactory
}