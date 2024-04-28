package ru.snowadv.emojis_data_api.model.di

import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.ZulipApi

interface EmojisDataModuleDependencies : BaseModuleDependencies {
    val dispatcherProvider: DispatcherProvider
    val api: ZulipApi
}