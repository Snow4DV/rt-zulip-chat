package ru.snowadv.messages_data_api.di

import ru.snowadv.data.api.AuthProvider
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.ZulipApi

interface MessagesDataModuleDependencies : BaseModuleDependencies {
    val dispatcherProvider: DispatcherProvider
    val api: ZulipApi
    val authProvider: AuthProvider
}