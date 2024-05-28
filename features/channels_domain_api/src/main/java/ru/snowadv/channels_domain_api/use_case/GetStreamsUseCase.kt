package ru.snowadv.channels_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_domain_api.model.Stream
import ru.snowadv.channels_domain_api.model.StreamType
import ru.snowadv.model.Resource

interface GetStreamsUseCase {
    operator fun invoke(type: StreamType): Flow<Resource<List<Stream>>>
}