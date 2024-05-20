package ru.snowadv.channels_data.di.holder

import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.database.dao.StreamsDao
import ru.snowadv.database.dao.TopicsDao
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.ZulipApi

interface ChannelsDataDependencies : BaseModuleDependencies {
    val zulipApi: ZulipApi
    val streamsDao: StreamsDao
    val topicsDao: TopicsDao
    val dispatcherProvider: DispatcherProvider
}