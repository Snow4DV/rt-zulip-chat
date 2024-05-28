package ru.snowadv.channels_presentation.di.holder

import android.content.Context
import ru.snowadv.channels_domain_api.use_case.GetStreamsUseCase
import ru.snowadv.channels_domain_api.use_case.GetTopicsUseCase
import ru.snowadv.channels_domain_api.use_case.ListenToStreamEventsUseCase
import ru.snowadv.channels_presentation.navigation.ChannelsRouter
import ru.snowadv.model.BaseUrlProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface ChannelsPresentationDependencies : BaseModuleDependencies {
    val channelsRouter: ChannelsRouter

    val getStreamsUseCase: GetStreamsUseCase
    val getTopicsUseCase: GetTopicsUseCase
    val listenToStreamEventsUseCase: ListenToStreamEventsUseCase

    val appContext: Context
}