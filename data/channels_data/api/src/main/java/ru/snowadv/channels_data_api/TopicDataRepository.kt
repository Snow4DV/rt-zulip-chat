package ru.snowadv.channels_data_api

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_data_api.model.DataTopic
import ru.snowadv.model.Resource

interface TopicDataRepository {

    fun getTopics(streamId: Long): Flow<Resource<List<DataTopic>>>
}