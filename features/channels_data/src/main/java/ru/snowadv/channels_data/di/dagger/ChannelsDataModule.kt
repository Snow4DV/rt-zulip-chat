package ru.snowadv.channels_data.di.dagger

import dagger.Binds
import dagger.Module
import ru.snowadv.channels_data.repository.StreamRepositoryImpl
import ru.snowadv.channels_data.repository.TopicRepositoryImpl
import ru.snowadv.channels_domain_api.repository.StreamRepository
import ru.snowadv.channels_domain_api.repository.TopicRepository

@Module
internal interface ChannelsDataModule {
    @Binds
    fun bindsStreamRepositoryImpl(impl: StreamRepositoryImpl): StreamRepository
    @Binds
    fun bindsTopicRepositoryImpl(impl: TopicRepositoryImpl): TopicRepository
}