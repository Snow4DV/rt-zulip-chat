package ru.snowadv.messages_data_impl

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.auth_data_api.AuthProvider
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.messages_data_api.MessageDataRepository
import ru.snowadv.messages_data_api.model.DataPaginatedMessages
import ru.snowadv.messages_data_impl.util.MessagesMapper.toDataPaginatedMessages
import ru.snowadv.messages_data_impl.util.MessagesMapper.toEntityMessages
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.InputStreamOpener
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.api.uploadFile
import ru.snowadv.network.model.NarrowRequestDto
import ru.snowadv.network.model.NarrowListRequestDto
import ru.snowadv.utils.foldToResource
import ru.snowadv.utils.toResource
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@Reusable
class MessageDataRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val authProvider: AuthProvider,
    private val api: ZulipApi,
    private val messagesDao: MessagesDao,
) : MessageDataRepository {
    companion object {
        const val NEWEST_MESSAGE_ANCHOR = "newest"
    }

    private val currentUserId get() = authProvider.getAuthorizedUser().id
    override fun getMessages(
        streamName: String,
        topicName: String,
        includeAnchorMessage: Boolean,
        countOfMessages: Int,
        anchorMessageId: Long?,
        showAndUpdateCache: Boolean,
    ): Flow<Resource<DataPaginatedMessages>> =
        flow {

            val cachedData = if (showAndUpdateCache) {
                messagesDao.getMessagesFromTopic(streamName, topicName)
                    .ifEmpty { null }
                    ?.toDataPaginatedMessages(
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
                if (showAndUpdateCache) {
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
                    messagesDto.toDataPaginatedMessages(
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
        mimeType: String?,
        inputStreamOpener: InputStreamOpener,
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

    override fun addReactionToMessage(messageId: Long, reactionName: String): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            emit(api.addReaction(messageId, reactionName).toResource())
        }.flowOn(dispatcherProvider.io)

    override fun removeReactionFromMessage(
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