package ru.snowadv.voiceapp.glue.repository

import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.snowadv.channels.domain.model.Stream
import ru.snowadv.channels.domain.model.Topic
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.channels.domain.repository.TopicRepository
import ru.snowadv.channels_data.api.StreamDataRepository
import ru.snowadv.channels_data.api.TopicDataRepository
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.voiceapp.glue.util.CommonMappers.mapListContent
import ru.snowadv.voiceapp.glue.util.ChannelsDataMappers.toChannelStream
import ru.snowadv.voiceapp.glue.util.ChannelsDataMappers.toChannelTopic
import javax.inject.Inject

@Reusable
class ChannelsRepositoryImpl @Inject constructor(
    private val streamDataRepository: StreamDataRepository,
    private val topicDataRepository: TopicDataRepository,
    private val dispatcherProvider: DispatcherProvider,
): StreamRepository, TopicRepository {
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