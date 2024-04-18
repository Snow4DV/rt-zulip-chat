package ru.snowadv.voiceapp.di.deps

import ru.snowadv.channels.di.ChannelsDeps
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.channels.domain.repository.TopicRepository
import ru.snowadv.people.di.PeopleDeps
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.people.domain.repository.PeopleRepository
import ru.snowadv.voiceapp.di.MainGraph
import ru.snowadv.voiceapp.glue.navigation.ChannelsRouterImpl
import ru.snowadv.voiceapp.glue.navigation.PeopleRouterImpl
import ru.snowadv.voiceapp.glue.repository.ChannelsRepositoryImpl
import ru.snowadv.voiceapp.glue.repository.PeopleRepositoryImpl

class PeopleDepsProvider : PeopleDeps {
    override val router: PeopleRouter by lazy { PeopleRouterImpl(MainGraph.mainDepsProvider.router) }
    override val peopleRepository: PeopleRepository by lazy { PeopleRepositoryImpl(MainGraph.mainDepsProvider.userDataRepository) }
}