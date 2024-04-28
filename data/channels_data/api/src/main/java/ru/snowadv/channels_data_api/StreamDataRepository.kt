package ru.snowadv.channels_data_api

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_data_api.model.DataStream
import ru.snowadv.model.Resource

interface StreamDataRepository {

    fun getStreams(): Flow<Resource<List<DataStream>>>
    fun getSubscribedStreams(): Flow<Resource<List<DataStream>>>
}