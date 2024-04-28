package ru.snowadv.events_data_api.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.ZulipApi

interface EventsDataModuleDependencies : BaseModuleDependencies {
    val dispatcherProvider: DispatcherProvider
    val api: ZulipApi
    val authProvider: AuthProvider
}
