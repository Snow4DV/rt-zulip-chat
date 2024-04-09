package ru.snowadv.voiceapp.glue.repository

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
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.voiceapp.glue.util.toChannelStream
import ru.snowadv.voiceapp.glue.util.toChannelTopic

class ChannelsRepositoryImpl(
    private val streamDataRepository: StreamDataRepository,
    private val topicDataRepository: TopicDataRepository,
): StreamRepository, TopicRepository {
    override fun getStreams(): Flow<Resource<List<Stream>>> {
        return streamDataRepository.getStreams()
            .map { it.map { it.map { it.toChannelStream() } } }
            .flowOn(Dispatchers.Default)
    }

    override fun getSubscribedStreams(): Flow<Resource<List<Stream>>>{
        return streamDataRepository.getSubscribedStreams()
            .map { it.map { it.map { it.toChannelStream() } } }
            .flowOn(Dispatchers.Default)
    }

    override fun getTopics(streamId: Long): Flow<Resource<List<Topic>>> {
        return topicDataRepository.getTopics(streamId)
            .map { it.map { it.map { it.toChannelTopic() } } }
            .flowOn(Dispatchers.Default)
    }
}