package ru.snowadv.channels_impl.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_impl.domain.model.Stream
import ru.snowadv.model.Resource

interface StreamRepository {

    fun getStreams(): Flow<Resource<List<Stream>>>
    fun getSubscribedStreams(): Flow<Resource<List<Stream>>>
}