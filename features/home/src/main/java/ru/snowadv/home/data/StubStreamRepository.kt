package ru.snowadv.home.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.domain.model.Resource
import ru.snowadv.home.domain.model.Stream
import ru.snowadv.home.domain.repository.StreamRepository

internal object StubStreamRepository: StreamRepository {
    override fun getStreams(): Flow<Resource<List<Stream>>> = flow {
        emit(Resource.Loading)
        delay(500)

        emit(Resource.Success(StubData.streams))
    }

    override fun getSubscribedStreams(): Flow<Resource<List<Stream>>> = flow {
        emit(Resource.Loading)
        delay(200)
        emit(Resource.Success(StubData.streams.filter { it.id in StubData.subscribedStreamsIds }))
    }

}