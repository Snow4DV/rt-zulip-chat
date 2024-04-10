package ru.snowadv.channels.data.stub

import ru.snowadv.channels.domain.model.Stream
import ru.snowadv.channels.domain.model.Topic

internal object StubData {
    val subscribedStreamsIds = setOf(1L, 2L)
    val streams = listOf(
        Stream(1, "general"),
        Stream(2, "Development"),
        Stream(3, "Design"),
        Stream(4, "PR"),
    )

    val topics = listOf(
        Topic(
            getUniqueIdForTopic(1, "Testing"),
            "Testing",
            1,
        ),
        Topic(
            getUniqueIdForTopic(1, "Bruh"),
            "Bruh",
            1,
        ),
        Topic(
            getUniqueIdForTopic(2, "BlahBlahBlah"),
            "BlahBlahBlah",
            2,
        ),
        Topic(
            getUniqueIdForTopic(2, "HihankiDaHahanki"),
            "HihankiDaHahanki",
            2,
        ),
        Topic(
            getUniqueIdForTopic(3, "Generic"),
            "Generic",
            3,
        ),
    )

    val topicsMap = topics.groupBy { it.streamId }

    // These function will be used while creating unique IDs in Data -> Domain mapper
    private fun getUniqueIdForTopic(streamId: Long, topicName: String) = "${streamId}_${topicName}"
}