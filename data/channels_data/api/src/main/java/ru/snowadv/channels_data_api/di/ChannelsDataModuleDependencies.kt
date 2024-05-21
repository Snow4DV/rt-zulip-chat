package ru.snowadv.channels_data_api.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.snowadv.database.dao.StreamsDao
import ru.snowadv.database.dao.TopicsDao
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.ZulipApi

interface ChannelsDataModuleDependencies : BaseModuleDependencies {
    val dispatcherProvider: DispatcherProvider
    val api: ZulipApi
    val streamsDao: StreamsDao
    val topicsDao: TopicsDao
}