package ru.snowadv.chat_impl.data.repository

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.snowadv.chat_impl.domain.model.ChatEmoji
import ru.snowadv.chat_impl.domain.model.ChatPaginatedMessages
import ru.snowadv.chat_impl.domain.repository.EmojiRepository
import ru.snowadv.chat_impl.domain.repository.MessageRepository
import ru.snowadv.chat_impl.data.util.MessageDataMappers.toChatEmoji
import ru.snowadv.chat_impl.data.util.MessageDataMappers.toChatPaginatedMessages
import ru.snowadv.emojis_data_api.model.EmojiDataRepository
import ru.snowadv.messages_data_api.MessageDataRepository
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import javax.inject.Inject

@Reusable
class ChatRepositoryImpl @Inject constructor(
    private val messageDataRepository: MessageDataRepository,
    private val emojiDataRepository: EmojiDataRepository,
    private val dispatcherProvider: DispatcherProvider,
) : MessageRepository,
    EmojiRepository {
    override fun getAvailableEmojis(): Flow<Resource<List<ChatEmoji>>> {
        return emojiDataRepository.getAvailableEmojis()
            .map { res ->
                res.map { dataEmojis ->
                    dataEmojis.map { emoji ->
                        emoji.toChatEmoji()
                    }
                }
            }
            .flowOn(dispatcherProvider.default)
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
        ).map { res ->
            res.map { dataPagMes ->
                dataPagMes.toChatPaginatedMessages()
            }
        }.flowOn(dispatcherProvider.default)
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
