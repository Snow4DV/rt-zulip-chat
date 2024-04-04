package ru.snowadv.channels.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.channels.data.stub.StubData
import ru.snowadv.channels.domain.model.Topic
import ru.snowadv.channels.domain.repository.TopicRepository
import ru.snowadv.domain.model.Resource
import java.io.IOException

internal object StubTopicRepository: TopicRepository {
    private var loadCounter = 0
    override fun getTopics(streamId: Long): Flow<Resource<List<Topic>>> = flow {
        emit(Resource.Loading)
        delay(2000)

        StubData.topicsMap[streamId]?.let {
            if (loadCounter++ % 2 == 0) {
                emit(Resource.Success(it))
            } else {
                emit(Resource.Error(IOException()))
            }
        } ?: run {
            emit(Resource.Success(emptyList()))
        }
    }
}