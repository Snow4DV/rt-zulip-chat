package ru.snowadv.home.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.domain.model.Resource
import ru.snowadv.home.domain.model.Stream

internal interface StreamRepository {

    fun getStreams(): Flow<Resource<List<Stream>>>
    fun getSubscribedStreams(): Flow<Resource<List<Stream>>>
}