package ru.snowadv.events_impl.di.holder

import ru.snowadv.events_api.repository.EventRepository
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.network.api.ZulipApi

interface EventsDataAPI : BaseModuleAPI {
    val dispatcherProvider: DispatcherProvider
    val eventRepo: EventRepository
}