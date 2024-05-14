package ru.snowadv.channels_api.di

import ru.snowadv.channels_api.presentation.ChannelsScreenFactory
import ru.snowadv.module_injector.module.BaseModuleAPI

interface ChannelsFeatureAPI : BaseModuleAPI {
    val screenFactory: ChannelsScreenFactory
}