package ru.snowadv.channels.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels.domain.model.Topic
import ru.snowadv.channels.domain.repository.TopicRepository
import ru.snowadv.model.Resource

internal class GetTopicsUseCase(
    private val topicRepository: TopicRepository,
) {
    operator fun invoke(streamId: Long): Flow<Resource<List<Topic>>> {
        return topicRepository.getTopics(streamId)
    }
}