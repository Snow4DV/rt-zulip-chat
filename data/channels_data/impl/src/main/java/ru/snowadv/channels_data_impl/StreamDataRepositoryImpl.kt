package ru.snowadv.channels_data_impl

import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.channels_data_api.StreamDataRepository
import ru.snowadv.channels_data_api.model.DataStream
import ru.snowadv.channels_data_impl.util.ChannelsMapper.toDataStream
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.utils.foldToResource
import javax.inject.Inject

@Reusable
internal class StreamDataRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val api: ZulipApi,
): StreamDataRepository {
    override fun getStreams(): Flow<Resource<List<DataStream>>> = flow {
        emit(Resource.Loading)
        emit(api.getAllStreams().foldToResource { streamsDto -> streamsDto.streams.map { it.toDataStream() } })
    }.flowOn(dispatcherProvider.io)

    override fun getSubscribedStreams(): Flow<Resource<List<DataStream>>> = flow {
        emit(Resource.Loading)
        emit(api.getSubscribedStreams().foldToResource { streamsDto -> streamsDto.subscriptions.map { it.toDataStream() } })
    }.flowOn(dispatcherProvider.io)
}