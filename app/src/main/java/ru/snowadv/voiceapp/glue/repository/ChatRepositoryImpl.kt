package ru.snowadv.voiceapp.glue.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.chat.domain.model.ChatEmoji
import ru.snowadv.chat.domain.model.ChatPaginatedMessages
import ru.snowadv.chat.domain.repository.EmojiRepository
import ru.snowadv.chat.domain.repository.MessageRepository
import ru.snowadv.emojis_data.api.EmojiDataRepository
import ru.snowadv.message_data.api.MessageDataRepository
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.voiceapp.glue.util.mapNotNullListContent
import ru.snowadv.voiceapp.glue.util.toChatEmoji
import ru.snowadv.voiceapp.glue.util.toChatMessage
import ru.snowadv.voiceapp.glue.util.toChatPaginatedMessages

class ChatRepositoryImpl(
    private val messageDataRepository: MessageDataRepository,
    private val emojiDataRepository: EmojiDataRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) : MessageRepository, EmojiRepository {
    override fun getAvailableEmojis(): Flow<Resource<List<ChatEmoji>>> {
        return emojiDataRepository.getAvailableEmojis()
            .map {
                it.map { dataEmojis ->
                    dataEmojis.map { emoji ->
                        emoji.toChatEmoji()
                    }
                }
            }
            .flowOn(defaultDispatcher)
    }

    override fun getMessages(
        streamName: String,
        topicName: String,
        includeAnchorMessage: Boolean,
        anchorMessageId: Long?,
        countOfMessages: Int
    ): Flow<Resource<ChatPaginatedMessages>> {
        return messageDataRepository.getMessages(
            streamName = streamName,
            topicName = topicName,
            includeAnchorMessage = includeAnchorMessage,
            countOfMessages = countOfMessages,
            anchorMessageId = anchorMessageId
        ).map {
            it.map { dataPagMes ->
                dataPagMes.toChatPaginatedMessages()
            }
        }.flowOn(defaultDispatcher)
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
