package ru.snowadv.channels.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.channels.data.stub.StubData
import ru.snowadv.channels.domain.model.Topic
import ru.snowadv.channels.domain.repository.TopicRepository
import ru.snowadv.domain.model.Resource

internal object StubTopicRepository: TopicRepository {
    override fun getTopics(streamId: Long): Flow<Resource<List<Topic>>> = flow {
        emit(Resource.Loading)
        delay(2000)

        StubData.topicsMap[streamId]?.let {
            emit(Resource.Success(it))
        } ?: run {
            emit(Resource.Success(emptyList()))
        }
    }
}