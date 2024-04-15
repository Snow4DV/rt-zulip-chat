package ru.snowadv.message_data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.message_data.api.MessageDataRepository
import ru.snowadv.message_data.model.DataMessage
import ru.snowadv.message_data.util.toDataMessage
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.model.NarrowDto
import ru.snowadv.network.model.NarrowListDto
import ru.snowadv.utils.foldToResource
import ru.snowadv.utils.toResource

class MessageDataRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val authProvider: AuthProvider,
    private val api: ZulipApi,
) : MessageDataRepository {
    private val currentUserId get() = authProvider.getAuthorizedUser().id
    override fun getMessages(streamName: String, topicName: String): Flow<Resource<List<DataMessage>>> =
        flow {
            emit(Resource.Loading)
            api.getMessages(numBefore = 100, narrow = NarrowListDto(NarrowDto.ofStreamAndTopic(streamName, topicName)), numAfter = 0)
                .foldToResource { messagesDto -> messagesDto.messages.map { messageDto -> messageDto.toDataMessage(currentUserId) } }
                .let { res -> emit(res) }
        }.flowOn(ioDispatcher)

    override fun sendMessage(
        streamName: String,
        topicName: String,
        text: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        emit(api.sendMessage(stream = streamName,  topic = topicName, content = text).toResource())
    }.flowOn(ioDispatcher)

    override fun addReactionToMessage(messageId: Long, reactionName: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        emit(api.addReaction(messageId, reactionName).toResource())
    }.flowOn(ioDispatcher)

    override fun removeReactionFromMessage(
        messageId: Long,
        reactionName: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        emit(api.removeReaction(messageId, reactionName).toResource())
    }.flowOn(ioDispatcher)
}