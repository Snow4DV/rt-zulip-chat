package ru.snowadv.channels.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels.data.repository.StubStreamRepository
import ru.snowadv.channels.data.repository.StubTopicRepository
import ru.snowadv.channels.domain.model.Stream
import ru.snowadv.channels.domain.model.Topic
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.channels.domain.repository.TopicRepository
import ru.snowadv.domain.model.Resource

internal class GetTopicsUseCase(
    private val topicRepository: TopicRepository = StubTopicRepository
) {
    operator fun invoke(streamId: Long): Flow<Resource<List<Topic>>> {
        return topicRepository.getTopics(streamId)
    }
}