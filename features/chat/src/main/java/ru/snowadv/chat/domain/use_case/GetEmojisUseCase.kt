package ru.snowadv.chat.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.snowadv.chat.domain.model.ChatEmoji
import ru.snowadv.chat.domain.repository.EmojiRepository
import ru.snowadv.model.Resource

class GetEmojisUseCase(private val emojiRepository: EmojiRepository) {
    operator fun invoke(): Flow<Resource<List<ChatEmoji>>> {
        return emojiRepository.getAvailableEmojis()
    }
}