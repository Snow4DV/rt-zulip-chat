package ru.snowadv.channels_data.di.holder

import ru.snowadv.channels_domain_api.repository.StreamRepository
import ru.snowadv.channels_domain_api.repository.TopicRepository
import ru.snowadv.module_injector.module.BaseModuleAPI

interface ChannelsDataAPI : BaseModuleAPI {
    val streamRepo: StreamRepository
    val topicRepo: TopicRepository
}