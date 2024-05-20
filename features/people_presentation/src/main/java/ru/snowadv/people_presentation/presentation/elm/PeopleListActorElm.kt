package ru.snowadv.people_presentation.presentation.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.model.Resource
import ru.snowadv.people_presentation.navigation.PeopleRouter
import ru.snowadv.people_presentation.ui.util.PeopleMappers.toElmEvent
import ru.snowadv.people_presentation.ui.util.PeopleMappers.toUiModel
import ru.snowadv.people_presentation.ui.util.PeopleMappers.toUpdateQueueDataElmEvent
import ru.snowadv.users_domain_api.use_case.GetPeopleUseCase
import ru.snowadv.users_domain_api.use_case.ListenToPeoplePresenceEventsUseCase
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

@Reusable
internal class PeopleListActorElm @Inject constructor(
    private val getPeopleUseCase: GetPeopleUseCase,
    private val listenToPresenceEventsUseCase: ListenToPeoplePresenceEventsUseCase,
    private val router: PeopleRouter,
) : Actor<PeopleListCommandElm, PeopleListEventElm>() {
    override fun execute(command: PeopleListCommandElm): Flow<PeopleListEventElm> {
        return when (command) {
            PeopleListCommandElm.LoadData -> {
                getPeopleUseCase().map { res ->
                    when (res) {
                        is Resource.Error -> PeopleListEventElm.Internal.Error(
                            res.throwable,
                            res.data,
                        )

                        is Resource.Loading -> res.data?.let { data ->
                            PeopleListEventElm.Internal.PeopleLoaded(
                                data,
                                true,
                            )
                        } ?: PeopleListEventElm.Internal.Loading

                        is Resource.Success -> PeopleListEventElm.Internal.PeopleLoaded(
                            res.data,
                            false,
                        )
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