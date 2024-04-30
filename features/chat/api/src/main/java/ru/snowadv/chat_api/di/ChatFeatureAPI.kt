package ru.snowadv.chat_api.di

import ru.snowadv.chat_api.presentation.ChatScreenFactory
import ru.snowadv.module_injector.module.BaseModuleAPI

interface ChatFeatureAPI : BaseModuleAPI {
    val screenFactory: ChatScreenFactory
}