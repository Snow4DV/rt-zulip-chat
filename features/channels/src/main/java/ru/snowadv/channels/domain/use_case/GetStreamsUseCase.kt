package ru.snowadv.channels.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels.data.repository.StubStreamRepository
import ru.snowadv.channels.domain.model.Stream
import ru.snowadv.channels.domain.model.StreamType
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.domain.model.Resource

internal class GetStreamsUseCase(
    private val streamsRepo: StreamRepository = StubStreamRepository
) {
    operator fun invoke(type: StreamType): Flow<Resource<List<Stream>>> {
        return when(type) {
            StreamType.SUBSCRIBED -> streamsRepo.getSubscribedStreams()
            StreamType.ALL -> streamsRepo.getStreams()
        }
    }
}