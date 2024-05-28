package ru.snowadv.chat_presentation.di.holder

import ru.snowadv.chat_presentation.api.ChatScreenFactory
import ru.snowadv.module_injector.module.BaseModuleAPI

interface ChatPresentationAPI : BaseModuleAPI {
    val screenFactory: ChatScreenFactory
}