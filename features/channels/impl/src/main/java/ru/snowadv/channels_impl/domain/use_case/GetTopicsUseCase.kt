package ru.snowadv.channels_impl.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.snowadv.channels_impl.domain.model.Topic
import ru.snowadv.channels_impl.domain.repository.TopicRepository
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import javax.inject.Inject

internal class GetTopicsUseCase @Inject constructor(
    private val topicRepository: TopicRepository,
) {
    operator fun invoke(streamId: Long): Flow<Resource<List<Topic>>> {
        return topicRepository.getTopics(streamId).map { res -> res.map { list -> list.sortedBy { it.name } } }
    }
}