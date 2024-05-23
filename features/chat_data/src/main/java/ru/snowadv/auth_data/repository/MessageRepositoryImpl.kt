package ru.snowadv.auth_data.repository

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.auth_data.util.MessagesMapper.toChatPaginatedMessages
import ru.snowadv.auth_data.util.MessagesMapper.toEntityMessages
import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.chat_domain_api.model.ChatPaginatedMessages
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.InputStreamOpener
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.api.uploadFile
import ru.snowadv.network.model.NarrowListRequestDto
import ru.snowadv.network.model.NarrowRequestDto
import ru.snowadv.utils.foldToResource
import ru.snowadv.utils.toResource
import javax.inject.Inject

@Reusable
internal class MessageRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val authProvider: AuthProvider,
    private val api: ZulipApi,
    private val messagesDao: MessagesDao,
) : MessageRepository {

    companion object {
        const val NEWEST_MESSAGE_ANCHOR = "newest"
    }

    private val currentUserId get() = authProvider.getAuthorizedUser().id

    override fun getMessagesFromTopic(
        streamName: String,
        topicName: String,
        includeAnchorMessage: Boolean,
        anchorMessageId: Long?,
        countOfMessages: Int,
        useCache: Boolean
    ): Flow<Resource<ChatPaginatedMessages>> =
        flow {

            val cachedData = if (useCache) {
                messagesDao.getMessagesFromTopic(streamName, topicName)
                    .ifEmpty { null }
                    ?.toChatPaginatedMessages(
                        currentUserId,
                        anchorMessageId,
                        includeAnchorMessage
                    )
            } else {
                null
            }

            emit(Resource.Loading(cachedData))


            val remoteMessages = api.getMessages(
                numBefore = countOfMessages,
                narrow = NarrowListRequestDto(
                    NarrowRequestDto.ofStreamAndTopic(
                        streamName,
                        topicName
                    )
                ),
                numAfter = 0,
                anchor = anchorMessageId?.toString() ?: NEWEST_MESSAGE_ANCHOR,
                applyMarkdown = true,
            )

            remoteMessages.getOrNull()?.let { remoteMsgs ->
                if (useCache) {
                    messagesDao.updateMessagesForTopicIfChanged(
                        streamName = streamName,
                        topicName = topicName,
                        messages = remoteMsgs.toEntityMessages(streamName),
                    )
                }
            }

            remoteMessages.foldToResource(
                cachedData = cachedData,
                mapper = { messagesDto ->
                    messagesDto.toChatPaginatedMessages(
                        currentUserId,
                        anchorMessageId,
                        includeAnchorMessage
                    )
                },
            )
                .let { res -> emit(res) }
        }.flowOn(dispatcherProvider.io)


    override fun getMessagesFromStream(
        streamName: String,
        includeAnchorMessage: Boolean,
        anchorMessageId: Long?,
        countOfMessages: Int,
        useCache: Boolean
    ): Flow<Resource<ChatPaginatedMessages>> =
        flow {

            val cachedData = if (useCache) {
                messagesDao.getMessagesFromStream(streamName)
                    .ifEmpty { null }
                    ?.toChatPaginatedMessages(
                        currentUserId,
                        anchorMessageId,
                        includeAnchorMessage
                    )
            } else {
                null
            }

            emit(Resource.Loading(cachedData))


            val remoteMessages = api.getMessages(
                numBefore = countOfMessages,
                narrow = NarrowListRequestDto(
                    NarrowRequestDto.ofStream(streamName)
                ),
                numAfter = 0,
                anchor = anchorMessageId?.toString() ?: NEWEST_MESSAGE_ANCHOR,
                applyMarkdown = true,
            )

            remoteMessages.getOrNull()?.let { remoteMsgs ->
                if (useCache) {
                    messagesDao.updateMessagesForStreamIfChanged(
                        streamName = streamName,
                        messages = remoteMsgs.toEntityMessages(streamName),
                    )
                }
            }

            remoteMessages.foldToResource(
                cachedData = cachedData,
                mapper = { messagesDto ->
                    messagesDto.toChatPaginatedMessages(
                        currentUserId,
                        anchorMessageId,
                        includeAnchorMessage
                    )
                },
            )
                .let { res -> emit(res) }
        }.flowOn(dispatcherProvider.io)

    override fun sendMessage(
        streamName: String,
        topicName: String,
        text: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        emit(api.sendMessage(stream = streamName, topic = topicName, content = text).toResource())
    }.flowOn(dispatcherProvider.io)

    override fun sendFile(
        streamName: String,
        topicName: String,
        inputStreamOpener: InputStreamOpener,
        mimeType: String?,
        extension: String?
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        api.uploadFile(mimeType, inputStreamOpener, extension).fold(
            onSuccess = { uploadFile ->
                sendMessage(
                    streamName = streamName,
                    topicName = topicName,
                    text = constructMessageWithAttachment(extension?.let { "attachment.$extension" }
                        ?: "attachment", uploadFile.uri),
                ).collect { sendMessageRes -> emit(sendMessageRes) }
            },
            onFailure = {
                emit(Resource.Error(it))
            }
        )
    }

    override fun addReaction(messageId: Long, reactionName: String): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            emit(api.addReaction(messageId, reactionName).toResource())
        }.flowOn(dispatcherProvider.io)

    override fun removeReaction(
        messageId: Long,
        reactionName: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        emit(api.removeReaction(messageId, reactionName).toResource())
    }.flowOn(dispatcherProvider.io)


    private fun constructMessageWithAttachment(fileName: String, fileUri: String): String {
        return "[$fileName]($fileUri)"
    }
}
