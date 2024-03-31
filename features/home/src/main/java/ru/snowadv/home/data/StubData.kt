package ru.snowadv.home.data

import ru.snowadv.home.domain.model.Person
import ru.snowadv.home.domain.model.Stream
import ru.snowadv.home.domain.model.Topic

internal object StubData {
    val subscribedStreamsIds = setOf(1L, 2L)
    val streams = listOf(
        Stream(1, "#general"),
        Stream(2, "#Development"),
        Stream(3, "#Design"),
        Stream(4, "#PR"),
    )

    val topics = listOf(
        Topic(
            getUniqueIdForTopic(1, "Testing"),
            "Testing",
            1
        ),
        Topic(
            getUniqueIdForTopic(1, "Bruh"),
            "Bruh",
            1
        ),
        Topic(
            getUniqueIdForTopic(2, "BlahBlahBlah"),
            "BlahBlahBlah",
            2
        ),
        Topic(
            getUniqueIdForTopic(2, "HihankiDaHahanki"),
            "HihankiDaHahanki",
            2
        ),
        Topic(
            getUniqueIdForTopic(3, "Generic"),
            "Generic",
            3
        ),
    )

    val topicsMap = topics.groupBy { it.streamId }

    val people = listOf(
        Person(
            1,
            "Ivan Ivanov",
            "example@email.com",
            "https://secure.gravatar.com/avatar/818c212b9f8830dfef491b3f7da99a14?d=identicon&version=1",
            Person.Status.OFFLINE
        ),
        Person(
            2,
            "Anastasia Petrova",
            "example@email.com",
            null,
            Person.Status.IDLE
        ),
        Person(
            3,
            "Sarah Connor",
            "longmailtotesttv@email.com",
            "https://i.pinimg.com/originals/c7/b0/df/c7b0dfa5a79973cf410622b8ccab4d7c.jpg",
            Person.Status.ONLINE
        ),
        Person(
            4,
            "Richard Stallman",
            "test@test.com",
            null,
            Person.Status.OFFLINE
        )
    )

    // These function will be used while creating unique IDs in Data -> Domain mapper
    private fun getUniqueIdForTopic(streamId: Long, topicName: String) = "${streamId}_${topicName}"
}