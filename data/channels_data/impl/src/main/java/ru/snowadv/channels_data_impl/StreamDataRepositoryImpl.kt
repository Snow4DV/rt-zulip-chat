package ru.snowadv.channels_data_impl

import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.channels_data_api.StreamDataRepository
import ru.snowadv.channels_data_api.model.DataStream
import ru.snowadv.channels_data_impl.util.ChannelsMapper.toDataStream
import ru.snowadv.channels_data_impl.util.ChannelsMapper.toStreamEntity
import ru.snowadv.database.dao.StreamsDao
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.utils.foldToResource
import javax.inject.Inject

@Reusable
internal class StreamDataRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val api: ZulipApi,
    private val streamsDao: StreamsDao,
) : StreamDataRepository {
    override fun getStreams(): Flow<Resource<List<DataStream>>> = flow<Resource<List<DataStream>>> {
        val cachedData = streamsDao.getAll().map { it.toDataStream() }.ifEmpty { null }

        emit(Resource.Loading(cachedData))

        // Load from network
        val allStreams = api.getAllStreams()

        // Save to db if loaded
        allStreams.getOrNull()?.let { dto -> streamsDao.insertAllStreams(dto.streams.map { it.toStreamEntity(false) }) }

        // Emit result
        emit(allStreams.foldToResource(
            cachedData = cachedData,
            mapper = { streamsDto -> streamsDto.streams.map { it.toDataStream() } },
        ))
    }.flowOn(dispatcherProvider.io)

    override fun getSubscribedStreams(): Flow<Resource<List<DataStream>>> = flow {
        val cachedData = streamsDao.getSubscribed().map { it.toDataStream() }.ifEmpty { null }

        emit(Resource.Loading(cachedData))

        val subscribedStreams = api.getSubscribedStreams()

        subscribedStreams.getOrNull()?.let { dto ->
            streamsDao.insertSubscribedStreams(dto.subscriptions.map {
                it.toStreamEntity(true)
            })
        }

        emit(subscribedStreams.foldToResource(
            cachedData = cachedData,
            mapper = { streamsDto -> streamsDto.subscriptions.map { it.toDataStream() } },
        ))
    }.flowOn(dispatcherProvider.io)
}