package ru.snowadv.channels_impl.data.repository

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.snowadv.channels_impl.domain.model.Stream
import ru.snowadv.channels_impl.domain.model.Topic
import ru.snowadv.channels_impl.domain.repository.StreamRepository
import ru.snowadv.channels_data_api.StreamDataRepository
import ru.snowadv.channels_data_api.TopicDataRepository
import ru.snowadv.channels_impl.domain.repository.TopicRepository
import ru.snowadv.channels_impl.data.util.ChannelsDataMappers.toChannelStream
import ru.snowadv.channels_impl.data.util.ChannelsDataMappers.toChannelTopic
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.utils.CommonMappers.mapListContent
import javax.inject.Inject

@Reusable
class ChannelsRepositoryImpl @Inject constructor(
    private val streamDataRepository: StreamDataRepository,
    private val topicDataRepository: TopicDataRepository,
    private val dispatcherProvider: DispatcherProvider,
): StreamRepository,
    TopicRepository {
    override fun getStreams(): Flow<Resource<List<Stream>>> {
        return streamDataRepository.getStreams()
            .map { res -> res.mapListContent { it.toChannelStream() } }
            .flowOn(dispatcherProvider.default)
    }

    override fun getSubscribedStreams(): Flow<Resource<List<Stream>>>{
        return streamDataRepository.getSubscribedStreams()
            .map { res -> res.mapListContent { it.toChannelStream() } }
            .flowOn(dispatcherProvider.default)
    }

    override fun getTopics(streamId: Long): Flow<Resource<List<Topic>>> {
        return topicDataRepository.getTopics(streamId)
            .map { res -> res.mapListContent { it.toChannelTopic() } }
            .flowOn(dispatcherProvider.default)
    }
}