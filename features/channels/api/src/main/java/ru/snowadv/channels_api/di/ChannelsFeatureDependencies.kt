package ru.snowadv.channels_api.di

import ru.snowadv.channels_api.domain.navigation.ChannelsRouter
import ru.snowadv.channels_data_api.StreamDataRepository
import ru.snowadv.channels_data_api.TopicDataRepository
import ru.snowadv.events_api.domain.EventRepository
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface ChannelsFeatureDependencies: BaseModuleDependencies {
    val router: ChannelsRouter
    val streamDataRepo: StreamDataRepository
    val topicDataRepo: TopicDataRepository
    val eventRepo: EventRepository
    val dispatcherProvider: DispatcherProvider
}