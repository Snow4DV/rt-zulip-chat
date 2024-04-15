package ru.snowadv.voiceapp.glue.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.chat.domain.model.ChatEmoji
import ru.snowadv.chat.domain.repository.EmojiRepository
import ru.snowadv.chat.domain.repository.MessageRepository
import ru.snowadv.emojis_data.api.EmojiDataRepository
import ru.snowadv.message_data.api.MessageDataRepository
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.voiceapp.glue.util.mapNotNullListContent
import ru.snowadv.voiceapp.glue.util.toChatEmoji
import ru.snowadv.voiceapp.glue.util.toChatMessage

class ChatRepositoryImpl(
    private val messageDataRepository: MessageDataRepository,
    private val emojiDataRepository: EmojiDataRepository,
) : MessageRepository, EmojiRepository {
    override fun getAvailableEmojis(): Flow<Resource<List<ChatEmoji>>> {
        return emojiDataRepository.getAvailableEmojis()
            .map { it.map { it.map { it.toChatEmoji() } } }
            .flowOn(Dispatchers.Default)
    }

    override fun getMessages(
        streamName: String,
        topicName: String
    ): Flow<Resource<List<ChatMessage>>> {
        return messageDataRepository.getMessages(streamName, topicName)
            .map { it.map { it.map { it.toChatMessage() } } }.flowOn(Dispatchers.Default)
    }

    override fun sendMessage(
        streamName: String,
        topicName: String,
        text: String
    ): Flow<Resource<Unit>> {
        return messageDataRepository.sendMessage(streamName, topicName, text)
    }

    override fun addReaction(messageId: Long, reactionName: String): Flow<Resource<Unit>> {
        return messageDataRepository.addReactionToMessage(messageId, reactionName)
    }

    override fun removeReaction(messageId: Long, reactionName: String): Flow<Resource<Unit>> {
        return messageDataRepository.removeReactionFromMessage(messageId, reactionName)
    }
}
