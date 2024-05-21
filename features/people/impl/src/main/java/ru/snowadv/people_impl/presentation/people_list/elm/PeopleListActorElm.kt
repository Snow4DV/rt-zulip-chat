package ru.snowadv.people_impl.presentation.people_list.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.snowadv.events_api.domain.model.DomainEvent
import ru.snowadv.model.Resource
import ru.snowadv.people_api.domain.navigation.PeopleRouter
import ru.snowadv.people_impl.domain.use_case.GetPeopleUseCase
import ru.snowadv.people_impl.domain.use_case.ListenToPresenceEventsUseCase
import ru.snowadv.people_impl.presentation.util.PeopleMappers.toElmEvent
import ru.snowadv.people_impl.presentation.util.PeopleMappers.toUiModel
import ru.snowadv.people_impl.presentation.util.PeopleMappers.toUpdateQueueDataElmEvent
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

@Reusable
internal class PeopleListActorElm @Inject constructor(
    private val getPeopleUseCase: GetPeopleUseCase,
    private val listenToPresenceEventsUseCase: ListenToPresenceEventsUseCase,
    private val router: PeopleRouter,
) : Actor<PeopleListCommandElm, PeopleListEventElm>() {
    override fun execute(command: PeopleListCommandElm): Flow<PeopleListEventElm> {
        return when (command) {
            PeopleListCommandElm.LoadData -> {
                getPeopleUseCase().map { res ->
                    when (res) {
                        is Resource.Error -> PeopleListEventElm.Internal.Error(
                            res.throwable,
                            res.data?.map { it.toUiModel() },
                        )

                        is Resource.Loading -> res.data?.let { data ->
                            PeopleListEventElm.Internal.PeopleLoaded(
                                data.map { it.toUiModel() },
                                true,
                            )
                        } ?: PeopleListEventElm.Internal.Loading

                        is Resource.Success -> PeopleListEventElm.Internal.PeopleLoaded(
                            res.data.map { it.toUiModel() },
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