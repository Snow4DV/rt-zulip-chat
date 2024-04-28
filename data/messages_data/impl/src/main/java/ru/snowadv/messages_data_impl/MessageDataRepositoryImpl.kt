package ru.snowadv.messages_data_impl

import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.messages_data_api.MessageDataRepository
import ru.snowadv.messages_data_api.model.DataPaginatedMessages
import ru.snowadv.messages_data_impl.util.MessagesMapper.toDataPaginatedMessages
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.model.NarrowRequestDto
import ru.snowadv.network.model.NarrowListRequestDto
import ru.snowadv.utils.foldToResource
import ru.snowadv.utils.toResource
import javax.inject.Inject

@Reusable
class MessageDataRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val authProvider: AuthProvider,
    private val api: ZulipApi,
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
        anchorMessageId: Long?
    ): Flow<Resource<DataPaginatedMessages>> =
        flow {
            emit(Resource.Loading)
            api.getMessages(
                numBefore = countOfMessages,
                narrow = NarrowListRequestDto(NarrowRequestDto.ofStreamAndTopic(streamName, topicName)),
                numAfter = 0,
                anchor = anchorMessageId?.toString() ?: NEWEST_MESSAGE_ANCHOR
            )
                .foldToResource { messagesDto ->
                    messagesDto.toDataPaginatedMessages(
                        currentUserId,
                        anchorMessageId,
                        includeAnchorMessage
                    )
                }
                .let { res -> emit(res) }
        }.flowOn(dispatcherProvider.io)

    override fun sendMessage(
        streamName: String,
        topicName: String,
        text: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        emit(api.sendMessage(stream = streamName, topic = topicName, content = text).toResource())
    }.flowOn(dispatcherProvider.io)

    override fun addReactionToMessage(messageId: Long, reactionName: String): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading)
            emit(api.addReaction(messageId, reactionName).toResource())
        }.flowOn(dispatcherProvider.io)

    override fun removeReactionFromMessage(
        messageId: Long,
        reactionName: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        emit(api.removeReaction(messageId, reactionName).toResource())
    }.flowOn(dispatcherProvider.io)
}