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
import ru.snowadv.network.model.SubscriptionDetailsListRequestDto
import ru.snowadv.network.model.SubscriptionDetailsRequestDto
import ru.snowadv.network.model.UnsubscribeStreamsListRequestDto
import ru.snowadv.network.utils.NetworkUtils.getHttpExceptionCode
import ru.snowadv.utils.combineFold
import ru.snowadv.utils.combineWithCache
import ru.snowadv.utils.foldToResource
import ru.snowadv.utils.toResource
import javax.inject.Inject

@Reusable
internal class StreamRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val api: ZulipApi,
    private val streamsDao: StreamsDao,
) : StreamRepository {
    override fun getStreams(): Flow<Resource<List<Stream>>> = flow {
        val cachedData = streamsDao.getAll().map { it.toDomainStream() }.ifEmpty { null }

        emit(Resource.Loading(cachedData))

        // Load from network
        val allStreams = api.getAllStreams()
        val subscribedStreams = api.getSubscribedStreams()

        // Combine both sources
        val allDomainStreams = allStreams.combineFold(
            other = subscribedStreams,
            onBothSuccess = { allStreamsDto, subscribedStreamsDto ->
                val subscribedStreamIds = subscribedStreamsDto.subscriptions.map { it.id }.toSet()
                Resource.Success(
                    allStreamsDto.streams.map { it.toDomainStream(subscribed = it.id in subscribedStreamIds) }
                )
            },
            onFailure = { throwable ->
                Resource.Error(throwable)
            }
        )

        // Update cache
        allDomainStreams.data?.let { streams ->
            streamsDao.insertAllStreams(streams.map { it.toStreamEntity() })
        }

        // Emit result (cache-first)
        emit(allDomainStreams.combineWithCache(cachedData))
    }.flowOn(dispatcherProvider.io)

    override fun getSubscribedStreams(): Flow<Resource<List<Stream>>> = flow {
        val cachedData = streamsDao.getSubscribed().map { it.toDomainStream() }.ifEmpty { null }

        emit(Resource.Loading(cachedData))

        // Load from network
        val subscribedStreams = api.getSubscribedStreams()

        // Update cache if data fetched successfully
        subscribedStreams.getOrNull()?.let { dto ->
            streamsDao.insertSubscribedStreams(dto.subscriptions.map {
                it.toStreamEntity(true)
            })
        }

        // Emit cache-first data
        emit(
            subscribedStreams.foldToResource(
                cachedData = cachedData,
                mapper = { streamsDto -> streamsDto.subscriptions.map { it.toDomainStream(subscribed = true) } },
            )
        )
    }.flowOn(dispatcherProvider.io)

    override fun createStream(
        name: String,
        description: String?,
        announce: Boolean,
        showHistoryToNewSubs: Boolean
    ): Flow<Resource<Unit>> {
        return subscribeOrCreateStream(
            name = name,
            description = description,
            announce = announce,
            showHistoryToNewSubs = showHistoryToNewSubs,
        )
    }

    override fun subscribeToStream(name: String): Flow<Resource<Unit>> {
        return subscribeOrCreateStream(name = name)
    }

    override fun unsubscribeFromStream(name: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        api.unsubscribeFromStreams(UnsubscribeStreamsListRequestDto(listOf(name))).fold(
            onSuccess = { emit(Resource.Success(Unit)) },
            onFailure = {
                emit(
                    Resource.Error(
                        throwable = it,
                        errorCode = it.getHttpExceptionCode(),
                    )
                )
            },
        )
    }

    private fun subscribeOrCreateStream(
        name: String,
        description: String? = null,
        announce: Boolean? = null,
        showHistoryToNewSubs: Boolean? = null,
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        api.subscribeOrCreateStream(
            subscriptionsDetails = SubscriptionDetailsListRequestDto(
                listOf(
                    SubscriptionDetailsRequestDto(
                        name = name,
                        description = description,
                    ),
                )
            ),
            showHistoryToNewSubscribers = showHistoryToNewSubs,
            announceChannel = announce,
        ).fold(
            onSuccess = { emit(Resource.Success(Unit)) },
            onFailure = {
                emit(
                    Resource.Error(
                        throwable = it,
                        errorCode = it.getHttpExceptionCode()
                    )
                )
            },
        )
    }

}