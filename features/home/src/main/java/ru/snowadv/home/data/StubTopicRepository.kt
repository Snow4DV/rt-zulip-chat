package ru.snowadv.home.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.domain.model.Resource
import ru.snowadv.home.domain.model.Stream
import ru.snowadv.home.domain.model.Topic
import ru.snowadv.home.domain.repository.StreamRepository
import ru.snowadv.home.domain.repository.TopicRepository

internal object StubTopicRepository: TopicRepository {
    override fun getTopics(streamId: Long): Flow<Resource<List<Topic>>> = flow {
        emit(Resource.Loading)
        delay(150)

        StubData.topicsMap[streamId]?.let {
            emit(Resource.Success(it))
        } ?: run {
            emit(Resource.Success(emptyList()))
        }
    }
}