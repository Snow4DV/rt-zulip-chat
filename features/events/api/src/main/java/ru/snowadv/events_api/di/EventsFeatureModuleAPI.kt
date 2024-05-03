package ru.snowadv.events_api.di

import ru.snowadv.events_api.domain.EventRepository
import ru.snowadv.module_injector.module.BaseModuleAPI

interface EventsFeatureModuleAPI : BaseModuleAPI {
    val eventRepo: EventRepository
}