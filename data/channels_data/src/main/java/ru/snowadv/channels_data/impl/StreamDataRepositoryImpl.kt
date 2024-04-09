package ru.snowadv.channels_data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.channels_data.api.StreamDataRepository
import ru.snowadv.channels_data.model.DataStream
import ru.snowadv.channels_data.util.toDataStream
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.stub.StubZulipApi
import ru.snowadv.utils.foldToResource

class StreamDataRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
): StreamDataRepository {

    private val api: ZulipApi = StubZulipApi
    override fun getStreams(): Flow<Resource<List<DataStream>>> = flow {
        emit(Resource.Loading)
        emit(api.getAllStreams().foldToResource { streamsDto -> streamsDto.streams.map { it.toDataStream() } })
    }.flowOn(ioDispatcher)

    override fun getSubscribedStreams(): Flow<Resource<List<DataStream>>> = flow {
        emit(Resource.Loading)
        emit(api.getSubscribedStreams().foldToResource { streamsDto -> streamsDto.subscriptions.map { it.toDataStream() } })
    }.flowOn(ioDispatcher)
}