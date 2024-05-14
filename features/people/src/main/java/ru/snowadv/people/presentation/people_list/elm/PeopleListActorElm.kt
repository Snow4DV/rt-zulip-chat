package ru.snowadv.people.presentation.people_list.elm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.model.Resource
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.people.domain.use_case.GetPeopleUseCase
import ru.snowadv.people.domain.use_case.ListenToPresenceEventsUseCase
import ru.snowadv.people.presentation.util.PeopleMappers.toElmEvent
import ru.snowadv.people.presentation.util.PeopleMappers.toUiModel
import ru.snowadv.people.presentation.util.PeopleMappers.toUpdateQueueDataElmEvent
import vivid.money.elmslie.core.store.Actor

internal class PeopleListActorElm(
    private val getPeopleUseCase: GetPeopleUseCase,
    private val listenToPresenceEventsUseCase: ListenToPresenceEventsUseCase,
    private val router: PeopleRouter,
) : Actor<PeopleListCommandElm, PeopleListEventElm>() {
    override fun execute(command: PeopleListCommandElm): Flow<PeopleListEventElm> {
        return when(command) {
            PeopleListCommandElm.LoadData -> {
                getPeopleUseCase().map { res ->
                    when (res) {
                        is Resource.Error -> PeopleListEventElm.Internal.Error(res.throwable)
                        Resource.Loading -> PeopleListEventElm.Internal.Loading
                        is Resource.Success -> PeopleListEventElm.Internal.PeopleLoaded(res.data.map { it.toUiModel() })
                    }
                }
            }
            is PeopleListCommandElm.ObservePresence -> {
                listenToPresenceEventsUseCase(command.isRestart, command.queueProps).map {
                    when (it) {
                        is DomainEvent.PresenceDomainEvent -> it.toElmEvent()
                        is DomainEvent.RegisteredNewQueueEvent -> it.toElmEvent()
                        is DomainEvent.FailedFetchingQueueEvent -> it.toElmEvent()
                        else -> it.toUpdateQueueDataElmEvent()
                    }
                }
            }

            is PeopleListCommandElm.OpenProfile -> flow { router.openProfile(command.userId) }
        }
    }
}