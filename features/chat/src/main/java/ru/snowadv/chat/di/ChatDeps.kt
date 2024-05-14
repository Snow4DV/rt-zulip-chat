package ru.snowadv.chat.di

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.chat.domain.repository.EmojiRepository
import ru.snowadv.chat.domain.repository.MessageRepository
import ru.snowadv.event_api.repository.EventRepository

interface ChatDeps {
    val router: ChatRouter
    val emojiRepository: EmojiRepository
    val messageRepository: MessageRepository
    val eventRepository: EventRepository
    val defaultDispatcher: CoroutineDispatcher
}