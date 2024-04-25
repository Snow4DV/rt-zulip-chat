package ru.snowadv.voiceapp.di.deps

import kotlinx.coroutines.CoroutineDispatcher
import ru.snowadv.chat.di.ChatDeps
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.chat.domain.repository.EmojiRepository
import ru.snowadv.chat.domain.repository.MessageRepository
import ru.snowadv.event_api.repository.EventRepository
import ru.snowadv.voiceapp.di.MainGraph
import ru.snowadv.voiceapp.glue.navigation.ChatRouterImpl
import ru.snowadv.voiceapp.glue.repository.ChannelsRepositoryImpl
import ru.snowadv.voiceapp.glue.repository.ChatRepositoryImpl

class ChatDepsProvider: ChatDeps {
    private val chatRepository by lazy {
        with(MainGraph.mainDepsProvider) {
            ChatRepositoryImpl(
                messageDataRepository,
                emojiDataRepository,
                defaultDispatcher
            )
        }
    }
    override val router: ChatRouter by lazy { ChatRouterImpl(MainGraph.mainDepsProvider.router) }
    override val emojiRepository: EmojiRepository get() = chatRepository
    override val messageRepository: MessageRepository get() = chatRepository
    override val eventRepository: EventRepository get() = MainGraph.mainDepsProvider.eventDataRepository
    override val defaultDispatcher: CoroutineDispatcher get() = MainGraph.mainDepsProvider.defaultDispatcher
}