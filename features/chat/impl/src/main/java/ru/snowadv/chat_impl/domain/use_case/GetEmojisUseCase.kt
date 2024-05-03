package ru.snowadv.chat_impl.domain.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.snowadv.chat_impl.domain.model.ChatEmoji
import ru.snowadv.chat_impl.domain.repository.EmojiRepository
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import javax.inject.Inject

@Reusable
internal class GetEmojisUseCase @Inject constructor(private val emojiRepository: EmojiRepository) {

    companion object {
        const val POPULAR_START_EMOJI =
            "1f600" // List of emojis will be shifted left to start with popular emoji
    }

    operator fun invoke(): Flow<Resource<List<ChatEmoji>>> {
        return emojiRepository.getAvailableEmojis()
            .map { it.map { it.startWithPopularEmoji(POPULAR_START_EMOJI) } }
    }


    private fun List<ChatEmoji>.startWithPopularEmoji(popularEmojiCode: String): List<ChatEmoji> {
        if (this.isEmpty()) return emptyList()

        val indexOfPopularEmoji =
            indexOfFirst { it.code == popularEmojiCode }.let { if (it == -1) 0 else it }

        return buildList {
            for (i in indexOfPopularEmoji until this@startWithPopularEmoji.size) {
                add(this@startWithPopularEmoji[i])
            }

            for (i in 0 until indexOfPopularEmoji) {
                add(this@startWithPopularEmoji[i])
            }
        }
    }
}