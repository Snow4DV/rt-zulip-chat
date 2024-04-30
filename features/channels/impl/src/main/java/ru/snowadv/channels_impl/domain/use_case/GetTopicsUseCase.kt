package ru.snowadv.channels_impl.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_api.domain.model.Topic
import ru.snowadv.channels_api.domain.repository.TopicRepository
import ru.snowadv.model.Resource
import javax.inject.Inject

internal class GetTopicsUseCase @Inject constructor(
    private val topicRepository: TopicRepository,
) {
    operator fun invoke(streamId: Long): Flow<Resource<List<Topic>>> {
        return topicRepository.getTopics(streamId)
    }
}