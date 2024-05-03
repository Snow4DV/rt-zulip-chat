package ru.snowadv.channels_api.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.snowadv.channels_api.domain.navigation.ChannelsRouter
import ru.snowadv.channels_data_api.StreamDataRepository
import ru.snowadv.channels_data_api.TopicDataRepository
import ru.snowadv.events_data_api.EventRepository
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface ChannelsFeatureDependencies: BaseModuleDependencies {
    val router: ChannelsRouter
    val streamDataRepo: StreamDataRepository
    val topicDataRepo: TopicDataRepository
    val eventRepo: EventRepository
    val dispatcherProvider: DispatcherProvider
}