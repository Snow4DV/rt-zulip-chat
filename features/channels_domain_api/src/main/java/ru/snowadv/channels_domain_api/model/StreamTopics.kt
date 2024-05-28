package ru.snowadv.channels_domain_api.model

import ru.snowadv.model.Resource

data class StreamTopics(
    val streamId: Long,
    val topics: Resource<List<Topic>>,
)