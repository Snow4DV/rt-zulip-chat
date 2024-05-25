package ru.snowadv.auth_data.di.holder

import ru.snowadv.chat_domain_api.repository.EmojiRepository
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.module_injector.module.BaseModuleAPI

interface ChatDataAPI : BaseModuleAPI {
    val messageRepo: MessageRepository
    val emojisRepo: EmojiRepository
}