package ru.snowadv.channels_impl.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_impl.domain.model.Topic
import ru.snowadv.model.Resource

interface TopicRepository {

    fun getTopics(streamId: Long): Flow<Resource<List<Topic>>>
}