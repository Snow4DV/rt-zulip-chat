package ru.snowadv.channels_data_impl

import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.channels_data_api.TopicDataRepository
import ru.snowadv.channels_data_api.model.DataTopic
import ru.snowadv.channels_data_impl.util.ChannelsMapper.toDataTopic
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.utils.foldToResource
import javax.inject.Inject

@Reusable
internal class TopicDataRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val api: ZulipApi,
): TopicDataRepository {
    override fun getTopics(streamId: Long): Flow<Resource<List<DataTopic>>> = flow {
        emit(Resource.Loading)
        emit(api.getTopicsByChannel(streamId).foldToResource { topicsDto -> topicsDto.topics.map { it.toDataTopic(streamId) } })
    }.flowOn(dispatcherProvider.io)
}