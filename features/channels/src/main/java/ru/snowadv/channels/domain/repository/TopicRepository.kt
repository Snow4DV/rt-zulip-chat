package ru.snowadv.channels.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels.domain.model.Topic
import ru.snowadv.model.Resource

interface TopicRepository {

    fun getTopics(streamId: Long): Flow<Resource<List<Topic>>>
}