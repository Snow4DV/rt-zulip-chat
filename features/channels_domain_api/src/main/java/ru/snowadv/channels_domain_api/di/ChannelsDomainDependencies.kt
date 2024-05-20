package ru.snowadv.channels_domain_api.di

import ru.snowadv.channels_domain_api.repository.StreamRepository
import ru.snowadv.channels_domain_api.repository.TopicRepository
import ru.snowadv.channels_domain_api.use_case.GetStreamsUseCase
import ru.snowadv.channels_domain_api.use_case.GetTopicsUseCase
import ru.snowadv.channels_domain_api.use_case.ListenToStreamEventsUseCase
import ru.snowadv.events_api.repository.EventRepository
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface ChannelsDomainDependencies: BaseModuleDependencies {
    val streamRepository: StreamRepository
    val topicRepository: TopicRepository
    val eventRepository: EventRepository
}