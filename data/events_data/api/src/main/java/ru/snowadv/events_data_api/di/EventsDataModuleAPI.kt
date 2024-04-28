package ru.snowadv.events_data_api.di

import ru.snowadv.events_data_api.EventRepository
import ru.snowadv.module_injector.module.BaseModuleAPI

interface EventsDataModuleAPI : BaseModuleAPI {
    val eventRepo: EventRepository
}