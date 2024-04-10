package ru.snowadv.channels.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.channels.data.stub.StubData
import ru.snowadv.channels.domain.model.Stream
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.domain.model.Resource

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