package ru.snowadv.channels_domain_api.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_domain_api.model.Topic
import ru.snowadv.model.Resource

interface TopicRepository {

    fun getTopics(streamId: Long): Flow<Resource<List<Topic>>>
}