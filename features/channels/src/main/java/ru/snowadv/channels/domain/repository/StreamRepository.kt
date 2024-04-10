package ru.snowadv.channels.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels.domain.model.Stream
import ru.snowadv.domain.model.Resource

internal interface StreamRepository {

    fun getStreams(): Flow<Resource<List<Stream>>>
    fun getSubscribedStreams(): Flow<Resource<List<Stream>>>
}