package ru.snowadv.channels_data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.channels_data.api.TopicDataRepository
import ru.snowadv.channels_data.model.DataTopic
import ru.snowadv.channels_data.util.toDataTopic
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.stub.StubZulipApi
import ru.snowadv.utils.foldToResource

class TopicDataRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val api: ZulipApi,
): TopicDataRepository {
    override fun getTopics(streamId: Long): Flow<Resource<List<DataTopic>>> = flow {
        emit(Resource.Loading)
        emit(api.getTopicsByChannel(streamId).foldToResource { topicsDto -> topicsDto.topics.map { it.toDataTopic(streamId) } })
    }.flowOn(ioDispatcher)
}