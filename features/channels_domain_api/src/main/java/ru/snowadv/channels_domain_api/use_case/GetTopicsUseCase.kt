package ru.snowadv.channels_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_domain_api.model.Stream
import ru.snowadv.channels_domain_api.model.Topic
import ru.snowadv.model.Resource

interface GetTopicsUseCase {
    operator fun invoke(streamId: Long): Flow<Resource<List<Topic>>>
}