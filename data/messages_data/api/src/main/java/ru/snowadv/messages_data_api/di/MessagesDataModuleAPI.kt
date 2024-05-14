package ru.snowadv.messages_data_api.di

import ru.snowadv.messages_data_api.MessageDataRepository
import ru.snowadv.module_injector.module.BaseModuleAPI

interface MessagesDataModuleAPI : BaseModuleAPI {
    val messageDataRepo: MessageDataRepository
}