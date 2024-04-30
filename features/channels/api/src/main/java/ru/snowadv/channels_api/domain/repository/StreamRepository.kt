package ru.snowadv.channels_api.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_api.domain.model.Stream
import ru.snowadv.model.Resource

interface StreamRepository {

    fun getStreams(): Flow<Resource<List<Stream>>>
    fun getSubscribedStreams(): Flow<Resource<List<Stream>>>
}