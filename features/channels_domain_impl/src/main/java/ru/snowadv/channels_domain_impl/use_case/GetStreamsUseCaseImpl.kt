package ru.snowadv.channels_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.snowadv.channels_domain_api.model.Stream
import ru.snowadv.channels_domain_api.model.StreamType
import ru.snowadv.channels_domain_api.repository.StreamRepository
import ru.snowadv.channels_domain_api.use_case.GetStreamsUseCase
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import javax.inject.Inject

@Reusable
internal class GetStreamsUseCaseImpl @Inject constructor(
    private val streamsRepo: StreamRepository,
): GetStreamsUseCase {
    override operator fun invoke(type: StreamType): Flow<Resource<List<Stream>>> {
        return when(type) {
            StreamType.SUBSCRIBED -> streamsRepo.getSubscribedStreams().map { res -> res.map { list -> list.sortedBy { it.name } } }
            StreamType.ALL -> streamsRepo.getStreams().map { res -> res.map { list -> list.sortedBy { it.name } } }
        }
    }
}