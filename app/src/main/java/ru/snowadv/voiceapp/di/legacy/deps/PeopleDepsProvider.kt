package ru.snowadv.voiceapp.di.legacy.deps

import kotlinx.coroutines.CoroutineDispatcher
import ru.snowadv.event_api.repository.EventRepository
import ru.snowadv.people_api.di.PeopleDeps
import ru.snowadv.people_api.domain.navigation.PeopleRouter
import ru.snowadv.people_api.domain.repository.PeopleRepository
import ru.snowadv.voiceapp.di.legacy.MainGraph
import ru.snowadv.voiceapp.glue.coroutines.DispatcherProviderImpl
import ru.snowadv.voiceapp.glue.navigation.PeopleRouterImpl
import ru.snowadv.voiceapp.glue.repository.PeopleRepositoryImpl

class PeopleDepsProvider : ru.snowadv.people_api.di.PeopleDeps {
    override val router: ru.snowadv.people_api.domain.navigation.PeopleRouter by lazy { PeopleRouterImpl(MainGraph.mainDepsProvider.router) }
    override val peopleRepository: ru.snowadv.people_api.domain.repository.PeopleRepository by lazy {
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