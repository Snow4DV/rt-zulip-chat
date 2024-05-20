package ru.snowadv.channels_data.repository

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.channels_data.util.ChannelsMapper.toDomainTopic
import ru.snowadv.channels_data.util.ChannelsMapper.toTopicEntity
import ru.snowadv.channels_domain_api.model.Topic
import ru.snowadv.channels_domain_api.repository.TopicRepository
import ru.snowadv.database.dao.TopicsDao
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.utils.foldToResource
import javax.inject.Inject

@Reusable
internal class TopicRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val api: ZulipApi,
    private val topicsDao: TopicsDao,
) : TopicRepository {
    override fun getTopics(streamId: Long): Flow<Resource<List<Topic>>> = flow {
        val cachedData = topicsDao.getTopicsByStreamId(streamId).map { it.toDomainTopic() }.ifEmpty { null }

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
                mapper = { topicsDto -> topicsDto.topics.map { it.toDomainTopic(streamId) } },
                cachedData = cachedData,
            ),
        )
    }.flowOn(dispatcherProvider.io)
}