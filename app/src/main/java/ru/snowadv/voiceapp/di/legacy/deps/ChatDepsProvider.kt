package ru.snowadv.voiceapp.di.legacy.deps

import kotlinx.coroutines.CoroutineDispatcher
import ru.snowadv.chat_api.domain.di.ChatDeps
import ru.snowadv.chat_api.domain.navigation.ChatRouter
import ru.snowadv.chat_api.domain.repository.EmojiRepository
import ru.snowadv.chat_api.domain.repository.MessageRepository
import ru.snowadv.event_api.repository.EventRepository
import ru.snowadv.voiceapp.di.legacy.MainGraph
import ru.snowadv.voiceapp.glue.coroutines.DispatcherProviderImpl
import ru.snowadv.voiceapp.glue.navigation.ChatRouterImpl
import ru.snowadv.voiceapp.glue.repository.ChatRepositoryImpl

class ChatDepsProvider: ru.snowadv.chat_api.domain.di.ChatDeps {
    private val chatRepository by lazy {
        with(MainGraph.mainDepsProvider) {
            ChatRepositoryImpl(
                messageDataRepository,
                emojiDataRepository,
                DispatcherProviderImpl()
            )
        }
    }
    override val router: ru.snowadv.chat_api.domain.navigation.ChatRouter by lazy { ChatRouterImpl(MainGraph.mainDepsProvider.router) }
    override val emojiRepository: ru.snowadv.chat_api.domain.repository.EmojiRepository get() = chatRepository
    override val messageRepository: ru.snowadv.chat_api.domain.repository.MessageRepository get() = chatRepository
    override val eventRepository: EventRepository get() = MainGraph.mainDepsProvider.eventDataRepository
    override val defaultDispatcher: CoroutineDispatcher get() = MainGraph.mainDepsProvider.defaultDispatcher
}