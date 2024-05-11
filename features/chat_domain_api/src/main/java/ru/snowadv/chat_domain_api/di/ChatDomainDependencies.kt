package ru.snowadv.chat_domain_api.di

import ru.snowadv.chat_domain_api.repository.EmojiRepository
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.events_api.repository.EventRepository
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface ChatDomainDependencies : BaseModuleDependencies {
    val emojiRepository: EmojiRepository
    val messageRepository: MessageRepository
    val eventRepository: EventRepository
}