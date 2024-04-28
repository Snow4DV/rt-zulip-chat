package ru.snowadv.channels_data_api.di

import ru.snowadv.channels_data_api.StreamDataRepository
import ru.snowadv.channels_data_api.TopicDataRepository
import ru.snowadv.module_injector.module.BaseModuleAPI

interface ChannelsDataModuleAPI : BaseModuleAPI {
    val streamDataRepo: StreamDataRepository
    val topicDataRepo: TopicDataRepository
}