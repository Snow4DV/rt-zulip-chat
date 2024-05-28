package ru.snowadv.channels_data.repository

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.channels_data.util.ChannelsMapper.toDomainStream
import ru.snowadv.channels_data.util.ChannelsMapper.toStreamEntity
import ru.snowadv.channels_domain_api.model.Stream
import ru.snowadv.channels_domain_api.repository.StreamRepository
import ru.snowadv.database.dao.StreamsDao
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.utils.foldToResource
import javax.inject.Inject

@Reusable
internal class StreamRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val api: ZulipApi,
    private val streamsDao: StreamsDao,
) : StreamRepository {
    override fun getStreams(): Flow<Resource<List<Stream>>> = flow<Resource<List<Stream>>> {
        val cachedData = streamsDao.getAll().map { it.toDomainStream() }.ifEmpty { null }

        emit(Resource.Loading(cachedData))

        // Load from network
        val allStreams = api.getAllStreams()

        // Save to db if loaded
        allStreams.getOrNull()?.let { dto -> streamsDao.insertAllStreams(dto.streams.map { it.toStreamEntity(false) }) }

        // Emit result
        emit(allStreams.foldToResource(
            cachedData = cachedData,
            mapper = { streamsDto -> streamsDto.streams.map { it.toDomainStream() } },
        ))
    }.flowOn(dispatcherProvider.io)

    override fun getSubscribedStreams(): Flow<Resource<List<Stream>>> = flow {
        val cachedData = streamsDao.getSubscribed().map { it.toDomainStream() }.ifEmpty { null }

        emit(Resource.Loading(cachedData))

        val subscribedStreams = api.getSubscribedStreams()

        subscribedStreams.getOrNull()?.let { dto ->
            streamsDao.insertSubscribedStreams(dto.subscriptions.map {
                it.toStreamEntity(true)
            })
        }

        emit(subscribedStreams.foldToResource(
            cachedData = cachedData,
            mapper = { streamsDto -> streamsDto.subscriptions.map { it.toDomainStream() } },
        ))
    }.flowOn(dispatcherProvider.io)
}