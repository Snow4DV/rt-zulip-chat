package ru.snowadv.channels_data_impl

import dagger.Reusable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.channels_data_api.TopicDataRepository
import ru.snowadv.channels_data_api.model.DataTopic
import ru.snowadv.channels_data_impl.util.ChannelsMapper.toDataTopic
import ru.snowadv.channels_data_impl.util.ChannelsMapper.toTopicEntity
import ru.snowadv.database.dao.TopicsDao
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.utils.foldToResource
import javax.inject.Inject

@Reusable
internal class TopicDataRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val api: ZulipApi,
    private val topicsDao: TopicsDao,
) : TopicDataRepository {
    override fun getTopics(streamId: Long): Flow<Resource<List<DataTopic>>> = flow {
        val cachedData = topicsDao.getTopicsByStreamId(streamId).map { it.toDataTopic() }.ifEmpty { null }

        emit(Resource.Loading(cachedData))

        // Load from network
        val topics = api.getTopicsByChannel(streamId)

        // Save to db if fetch is successful
        topics.getOrNull()?.let {
            topicsDao.insertTopicsIfChanged(
                streamId,
                it.topics.map { topic -> topic.toTopicEntity(streamId) })
        }

        // Emit result
        emit(
            topics.foldToResource(
                mapper = { topicsDto -> topicsDto.topics.map { it.toDataTopic(streamId) } },
                cachedData = cachedData,
            ),
        )
    }.flowOn(dispatcherProvider.io)
}