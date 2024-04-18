package ru.snowadv.message_data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.message_data.api.MessageDataRepository
import ru.snowadv.message_data.model.DataMessage
import ru.snowadv.message_data.util.toDataMessage
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.model.NarrowDto
import ru.snowadv.network.stub.StubZulipApi
import ru.snowadv.utils.foldToResource
import ru.snowadv.utils.toResource

class MessageDataRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
) : MessageDataRepository {
    private val api: ZulipApi = StubZulipApi
    override fun getMessages(streamName: String, topicName: String): Flow<Resource<List<DataMessage>>> =
        flow {
            emit(Resource.Loading)
            api.getMessages(NarrowDto.ofStreamAndTopic(streamName, topicName))
                .foldToResource { messagesDto -> messagesDto.messages.map { messageDto -> messageDto.toDataMessage() } }
                .let { res -> emit(res) }
        }.flowOn(ioDispatcher)

    override fun sendMessage(
        streamName: String,
        topicName: String,
        text: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        emit(api.sendMessage(streamName,  topicName, text).toResource())
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