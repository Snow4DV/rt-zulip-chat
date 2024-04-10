package ru.snowadv.profile.data.stub

import ru.snowadv.profile.domain.model.Person
internal object StubData {

    val people = listOf(
        Person(
            1,
            "Ivan Ivanov",
            "example@email.com",
            null,
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
            null,
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
}