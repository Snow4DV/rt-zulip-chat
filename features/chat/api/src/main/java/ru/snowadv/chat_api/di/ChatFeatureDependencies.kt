package ru.snowadv.chat_api.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.snowadv.chat_api.domain.navigation.ChatRouter
import ru.snowadv.chat_api.domain.repository.EmojiRepository
import ru.snowadv.chat_api.domain.repository.MessageRepository
import ru.snowadv.events_data_api.EventRepository
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface ChatFeatureDependencies : BaseModuleDependencies {
    val router: ChatRouter
    val emojiRepository: EmojiRepository
    val messageRepository: MessageRepository
    val eventRepository: EventRepository
    val dispatcherProvider: DispatcherProvider
}