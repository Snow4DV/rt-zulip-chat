package ru.snowadv.chat.domain.repository

import ru.snowadv.chat.domain.model.Emoji

internal interface EmojiRepository {
    fun getAvailableEmojis(): Collection<Emoji>
    fun getEmojiByCode(code: Int): Emoji?
    fun getEmojiByStringCode(code: String): Emoji?
}