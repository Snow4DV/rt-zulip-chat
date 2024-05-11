package ru.snowadv.events_impl.di.holder

import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.ZulipApi

interface EventsDataDependencies : BaseModuleDependencies {
    val dispatcherProvider: DispatcherProvider
    val authProvider: AuthProvider
    val api: ZulipApi
}