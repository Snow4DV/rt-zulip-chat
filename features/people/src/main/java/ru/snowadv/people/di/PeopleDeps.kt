package ru.snowadv.people.di

import ru.snowadv.event_api.repository.EventRepository
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.people.domain.repository.PeopleRepository

interface PeopleDeps {
    val router: PeopleRouter
    val peopleRepository: PeopleRepository
    val eventRepo: EventRepository
}