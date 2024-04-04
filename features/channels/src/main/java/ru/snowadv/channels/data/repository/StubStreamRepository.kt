package ru.snowadv.channels.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.channels.data.stub.StubData
import ru.snowadv.channels.domain.model.Stream
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.domain.model.Resource
import java.io.IOException

internal object StubStreamRepository: StreamRepository {
    private var loadCounter = 0
    override fun getStreams(): Flow<Resource<List<Stream>>> = flow {
        emit(Resource.Loading)
        delay(2000)

        if (loadCounter++ % 2 == 0) {
            emit(Resource.Success(StubData.streams))
        } else {
            emit(Resource.Error(IOException()))
        }
    }

    override fun getSubscribedStreams(): Flow<Resource<List<Stream>>> = flow {
        emit(Resource.Loading)
        delay(2000)
        if (loadCounter++ % 2 == 0) {
            emit(Resource.Success(StubData.streams.filter { it.id in StubData.subscribedStreamsIds }))
        } else {
            emit(Resource.Error(IOException()))
        }
    }

}