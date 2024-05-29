package ru.snowadv.channels_domain_api.di

import ru.snowadv.channels_domain_api.use_case.CreateStreamUseCase
import ru.snowadv.channels_domain_api.use_case.GetStreamsUseCase
import ru.snowadv.channels_domain_api.use_case.GetTopicsUseCase
import ru.snowadv.channels_domain_api.use_case.ListenToStreamEventsUseCase
import ru.snowadv.channels_domain_api.use_case.ChangeStreamSubscriptionStatusUseCase
import ru.snowadv.module_injector.module.BaseModuleAPI

interface ChannelsDomainAPI: BaseModuleAPI {
    val getStreamsUseCase: GetStreamsUseCase
    val getTopicsUseCase: GetTopicsUseCase
    val listenToStreamEventsUseCase: ListenToStreamEventsUseCase
    val createStreamUseCase: CreateStreamUseCase
    val subscribeToStreamUseCase: ChangeStreamSubscriptionStatusUseCase
}