package ru.snowadv.people_impl.presentation.people_list.elm

import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.events_api.helper.StateMachineQueueHelper
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.people_impl.presentation.model.Person
import ru.snowadv.model.ScreenState
import ru.snowadv.presentation.util.toScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

internal class PeopleListReducerElm @Inject constructor() :
    ScreenDslReducer<PeopleListEventElm, PeopleListEventElm.Ui, PeopleListEventElm.Internal, PeopleListStateElm, PeopleListEffectElm, PeopleListCommandElm>(
        uiEventClass = PeopleListEventElm.Ui::class,
        internalEventClass = PeopleListEventElm.Internal::class,
    ) {
    override fun Result.internal(event: PeopleListEventElm.Internal) {
        if (!StateMachineQueueHelper.determineIfEventIsByServerAndBelongsToStateOrOther(
                queueProps = state.eventQueueData, event = event
            )
        ) return
        when (event) {
            is PeopleListEventElm.Internal.Error -> state {
                copy(
                    screenState = ScreenState.Error(event.throwable, event.cachedPeople),
                    peopleRes = Resource.Error(event.throwable),
                    eventQueueData = null,
                )
            }

            PeopleListEventElm.Internal.Loading -> state {
                copy(
                    screenState = ScreenState.Loading(),
                    peopleRes = Resource.Loading(),
                    eventQueueData = null,
                )
            }

            is PeopleListEventElm.Internal.PeopleLoaded -> {
                state {
                    copy(
                        screenState = event.people.toScreenState()
                            .filterBySearchQuery(state.searchQuery),
                        peopleRes = Resource.Success(event.people),
                    )
                }
                if (state.isResumed && !event.cached) {
                    observeCommand()
                }
            }

            is PeopleListEventElm.Internal.ServerEvent.EventQueueFailed -> {
                if (event.recreateQueue) {
                    state { copy(eventQueueData = null) }
                    commands { +PeopleListCommandElm.LoadData }
                } else if (state.isResumed) {
                    commands {
                        +PeopleListCommandElm.ObservePresence(
                            isRestart = true,
                            queueProps = state.eventQueueData,
                        )
                    }
                }
            }

            is PeopleListEventElm.Internal.ServerEvent.EventQueueRegistered -> {
                state {
                    copy(
                        eventQueueData = EventQueueProperties(
                            event.queueId, event.timeoutSeconds, event.eventId
                        ),
                    )
                }
                observeCommand()
            }

            is PeopleListEventElm.Internal.ServerEvent.EventQueueUpdated -> {
                state {
                    copy(eventQueueData = eventQueueData?.copy(lastEventId = event.eventId))
                }
                observeCommand()
            }

            is PeopleListEventElm.Internal.ServerEvent.PresenceUpdated -> {
                state {
                    updateStatus(
                        userId = event.userId,
                        status = event.newStatus,
                        eventId = event.eventId
                    )
                }
                observeCommand()
            }
        }
    }

    override fun Result.ui(event: PeopleListEventElm.Ui) {
        when (event) {
            PeopleListEventElm.Ui.ClickedOnRetry, PeopleListEventElm.Ui.Init -> commands {
                +PeopleListCommandElm.LoadData
            }

            PeopleListEventElm.Ui.Paused -> state {
                copy(isResumed = false)
            }

            PeopleListEventElm.Ui.Resumed -> {
                state {
                    copy(isResumed = true)
                }
                observeCommand()
            }

            is PeopleListEventElm.Ui.ChangedSearchQuery -> if (state.searchQuery != event.query) {
                state {
                    copy(
                        searchQuery = event.query,
                        screenState = peopleRes.toScreenState().filterBySearchQuery(event.query),
                    )
                }
            }

            is PeopleListEventElm.Ui.ClickedOnPerson -> commands {
                +PeopleListCommandElm.OpenProfile(
                    event.userId
                )
            }

            PeopleListEventElm.Ui.ClickedOnSearchIcon -> effects {
                +PeopleListEffectElm.FocusOnSearchFieldAndOpenKeyboard
            }
        }
    }

    private fun Result.observeCommand() {
        if (!state.isResumed || state.screenState !is ScreenState.Success) return
        commands {
            +PeopleListCommandElm.ObservePresence(
                isRestart = false,
                queueProps = state.eventQueueData,
            )
        }
    }

    private fun ScreenState<List<Person>>.filterBySearchQuery(query: String): ScreenState<List<Person>> {
        return getCurrentData()
            ?.filter { it.fullName.contains(other = query, ignoreCase = true) }
            ?.let { if (it.isEmpty()) ScreenState.Empty else ScreenState.Success(it) } ?: this
    }

    private fun PeopleListStateElm.updateStatus(
        userId: Long,
        status: Person.Status,
        eventId: Long
    ): PeopleListStateElm {
        val peopleRes = peopleRes.map { people ->
            people.map { person ->
                if (person.id == userId) {
                    person.copy(status = status)
                } else {
                    person
                }
            }.sortedWith(compareBy({ it.status.ordinal }, { it.fullName }))
        }
        return copy(
            peopleRes = peopleRes,
            screenState = peopleRes.toScreenState().filterBySearchQuery(searchQuery),
            eventQueueData = eventQueueData?.copy(lastEventId = eventId)
        )
    }
}