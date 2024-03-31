package ru.snowadv.home.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.domain.model.Resource
import ru.snowadv.home.domain.model.Stream
import ru.snowadv.home.domain.model.Topic

internal interface TopicRepository {

    fun getTopics(streamId: Long): Flow<Resource<List<Topic>>>
}