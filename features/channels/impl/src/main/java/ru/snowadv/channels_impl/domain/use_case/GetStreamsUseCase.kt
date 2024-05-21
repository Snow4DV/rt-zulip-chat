package ru.snowadv.channels_impl.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.snowadv.channels_impl.domain.model.Stream
import ru.snowadv.channels_impl.domain.model.StreamType
import ru.snowadv.channels_impl.domain.repository.StreamRepository
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import javax.inject.Inject

internal class GetStreamsUseCase @Inject constructor(
    private val streamsRepo: StreamRepository,
) {
    operator fun invoke(type: StreamType): Flow<Resource<List<Stream>>> {
        return when(type) {
            StreamType.SUBSCRIBED -> streamsRepo.getSubscribedStreams().map { res -> res.map { list -> list.sortedBy { it.name } } }
            StreamType.ALL -> streamsRepo.getStreams().map { res -> res.map { list -> list.sortedBy { it.name } } }
        }
    }
}