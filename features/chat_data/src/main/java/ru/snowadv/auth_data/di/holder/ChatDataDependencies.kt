package ru.snowadv.auth_data.di.holder

import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.ZulipApi

interface ChatDataDependencies : BaseModuleDependencies {
    val zulipApi: ZulipApi
    val emojisDao: EmojisDao
    val messagesDao: MessagesDao
    val dispatcherProvider: DispatcherProvider
    val authProvider: AuthProvider
}