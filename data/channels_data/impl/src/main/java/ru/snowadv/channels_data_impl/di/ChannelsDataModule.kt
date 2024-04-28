package ru.snowadv.channels_data_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.channels_data_api.StreamDataRepository
import ru.snowadv.channels_data_api.TopicDataRepository
import ru.snowadv.channels_data_impl.StreamDataRepositoryImpl
import ru.snowadv.channels_data_impl.TopicDataRepositoryImpl

@Module
internal interface ChannelsDataModule {
    @Binds
    fun bindStreamDataRepoImpl(streamDataRepositoryImpl: StreamDataRepositoryImpl): StreamDataRepository
    @Binds
    fun bindTopicDataRepoImpl(topicDataRepositoryImpl: TopicDataRepositoryImpl): TopicDataRepository
}