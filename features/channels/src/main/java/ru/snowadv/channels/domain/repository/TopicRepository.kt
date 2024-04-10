package ru.snowadv.channels.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels.domain.model.Topic
import ru.snowadv.domain.model.Resource

internal interface TopicRepository {

    fun getTopics(streamId: Long): Flow<Resource<List<Topic>>>
}