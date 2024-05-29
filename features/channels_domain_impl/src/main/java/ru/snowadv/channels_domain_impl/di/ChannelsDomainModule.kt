package ru.snowadv.channels_domain_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.channels_domain_api.use_case.CreateStreamUseCase
import ru.snowadv.channels_domain_api.use_case.GetStreamsUseCase
import ru.snowadv.channels_domain_api.use_case.GetTopicsUseCase
import ru.snowadv.channels_domain_api.use_case.ListenToStreamEventsUseCase
import ru.snowadv.channels_domain_api.use_case.ChangeStreamSubscriptionStatusUseCase
import ru.snowadv.channels_domain_impl.use_case.CreateStreamUseCaseImpl
import ru.snowadv.channels_domain_impl.use_case.GetStreamsUseCaseImpl
import ru.snowadv.channels_domain_impl.use_case.GetTopicsUseCaseImpl
import ru.snowadv.channels_domain_impl.use_case.ListenToStreamEventsUseCaseImpl
import ru.snowadv.channels_domain_impl.use_case.ChangeStreamSubscriptionStatusUseCaseImpl

@Module
internal interface ChannelsDomainModule {
    @Binds
    fun bindGetStreamsUseCaseImpl(impl: GetStreamsUseCaseImpl): GetStreamsUseCase
    @Binds
    fun bindGetTopicsUseCaseImpl(impl: GetTopicsUseCaseImpl): GetTopicsUseCase
    @Binds
    fun bindListenToStreamEventsUseCaseImpl(impl: ListenToStreamEventsUseCaseImpl): ListenToStreamEventsUseCase
    @Binds
    fun bindCreateStreamUseCaseImpl(impl: CreateStreamUseCaseImpl): CreateStreamUseCase
    @Binds
    fun bindSubscribeToStreamUseCaseImpl(impl: ChangeStreamSubscriptionStatusUseCaseImpl): ChangeStreamSubscriptionStatusUseCase

}