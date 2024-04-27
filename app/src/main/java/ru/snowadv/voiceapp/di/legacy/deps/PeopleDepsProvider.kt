package ru.snowadv.voiceapp.di.legacy.deps

import kotlinx.coroutines.CoroutineDispatcher
import ru.snowadv.event_api.repository.EventRepository
import ru.snowadv.people.di.PeopleDeps
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.people.domain.repository.PeopleRepository
import ru.snowadv.voiceapp.di.legacy.MainGraph
import ru.snowadv.voiceapp.glue.coroutines.DispatcherProviderImpl
import ru.snowadv.voiceapp.glue.navigation.PeopleRouterImpl
import ru.snowadv.voiceapp.glue.repository.PeopleRepositoryImpl

class PeopleDepsProvider : PeopleDeps {
    override val router: PeopleRouter by lazy { PeopleRouterImpl(MainGraph.mainDepsProvider.router) }
    override val peopleRepository: PeopleRepository by lazy {
        PeopleRepositoryImpl(
            MainGraph.mainDepsProvider.userDataRepository,
            DispatcherProviderImpl(),
        )
    }
    override val eventRepo: EventRepository
        get() = MainGraph.mainDepsProvider.eventDataRepository
    override val defaultDispatcher: CoroutineDispatcher
        get() = MainGraph.mainDepsProvider.defaultDispatcher
}