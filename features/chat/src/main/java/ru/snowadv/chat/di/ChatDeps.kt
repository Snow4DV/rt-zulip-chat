package ru.snowadv.chat.di

import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.chat.domain.repository.EmojiRepository
import ru.snowadv.chat.domain.repository.MessageRepository

interface ChatDeps {
    val router: ChatRouter
    val emojiRepository: EmojiRepository
    val messageRepository: MessageRepository
}