package ru.snowadv.channels_data.api

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_data.model.DataStream
import ru.snowadv.model.Resource

interface StreamDataRepository {

    fun getStreams(): Flow<Resource<List<DataStream>>>
    fun getSubscribedStreams(): Flow<Resource<List<DataStream>>>
}