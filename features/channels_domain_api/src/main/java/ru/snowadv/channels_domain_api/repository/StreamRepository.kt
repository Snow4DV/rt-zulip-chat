package ru.snowadv.channels_domain_api.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_domain_api.model.Stream
import ru.snowadv.model.Resource

interface StreamRepository {

    fun getStreams(): Flow<Resource<List<Stream>>>
    fun getSubscribedStreams(): Flow<Resource<List<Stream>>>
    fun createStream(name: String, description: String? = null, announce: Boolean, showHistoryToNewSubs: Boolean): Flow<Resource<Unit>>
    fun subscribeToStream(name: String): Flow<Resource<Unit>>
    fun unsubscribeFromStream(name: String): Flow<Resource<Unit>>
}