package ru.snowadv.channels_domain_api.di

import ru.snowadv.channels_domain_api.use_case.GetStreamsUseCase
import ru.snowadv.channels_domain_api.use_case.GetTopicsUseCase
import ru.snowadv.channels_domain_api.use_case.ListenToStreamEventsUseCase
import ru.snowadv.module_injector.module.BaseModuleAPI

interface ChannelsDomainAPI: BaseModuleAPI {
    val getStreamsUseCase: GetStreamsUseCase
    val getTopicsUseCase: GetTopicsUseCase
    val listenToStreamEventsUseCase: ListenToStreamEventsUseCase
}