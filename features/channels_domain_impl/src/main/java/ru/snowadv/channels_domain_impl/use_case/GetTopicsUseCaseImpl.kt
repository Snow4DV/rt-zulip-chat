package ru.snowadv.channels_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.snowadv.channels_domain_api.model.Topic
import ru.snowadv.channels_domain_api.repository.TopicRepository
import ru.snowadv.channels_domain_api.use_case.GetTopicsUseCase
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import javax.inject.Inject

@Reusable
internal class GetTopicsUseCaseImpl @Inject constructor(
    private val topicRepository: TopicRepository,
): GetTopicsUseCase {
    override operator fun invoke(streamId: Long): Flow<Resource<List<ru.snowadv.channels_domain_api.model.Topic>>> {
        return topicRepository.getTopics(streamId).map { res -> res.map { list -> list.sortedBy { it.name } } }
    }
}