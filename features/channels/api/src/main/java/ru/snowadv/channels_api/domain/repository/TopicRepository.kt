package ru.snowadv.channels_api.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_api.domain.model.Topic
import ru.snowadv.model.Resource

interface TopicRepository {

    fun getTopics(streamId: Long): Flow<Resource<List<Topic>>>
}