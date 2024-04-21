package ru.snowadv.profile.presentation.profile.elm

import ru.snowadv.event_api.helper.StateMachineQueueHelper
import ru.snowadv.event_api.helper.EventQueueProperties
import ru.snowadv.presentation.model.ScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

internal class ProfileReducerElm  : ScreenDslReducer<ProfileEventElm, ProfileEventElm.Ui, ProfileEventElm.Internal, ProfileStateElm, ProfileEffectElm, ProfileCommandElm>(
    uiEventClass = ProfileEventElm.Ui::class,
    internalEventClass = ProfileEventElm.Internal::class,
) {
    override fun Result.internal(event: ProfileEventElm.Internal) {
        if (!StateMachineQueueHelper.determineIfEventIsByServerAndBelongsToStateOrOther(state.eventQueueData, event)) return
        when (event) {
            is ProfileEventElm.Internal.Error -> state {
                copy(
                    screenState = ScreenState.Error(event.throwable),
                    eventQueueData = null,
                )
            }

            is ProfileEventElm.Internal.PersonLoaded -> {
                state {
                    copy(
                        screenState = ScreenState.Success(event.person),
                        eventQueueData = null,
                    )
                }
                commands {
                    +ProfileCommandElm.ObservePresence(
                        profileId = state.profileId,
                        isRestart = false,
                        queueProps = null,
                    )
                }
            }

            ProfileEventElm.Internal.Loading -> state {
                copy(
                    screenState = ScreenState.Loading,
                    eventQueueData = null,
                )
            }

            is ProfileEventElm.Internal.ServerEvent.EventQueueFailed -> {
                state {
                    copy(eventQueueData = null, screenState = ScreenState.Error())
                }
            }

            is ProfileEventElm.Internal.ServerEvent.EventQueueRegistered -> {
                val queueProps = EventQueueProperties(
                    event.queueId,
                    event.timeoutSeconds,
                    event.eventId
                )
                state {
                    copy(
                        eventQueueData = queueProps,
                    )
                }
                commands {
                    +ProfileCommandElm.ObservePresence(
                        profileId = state.profileId,
                        isRestart = false,
                        queueProps = queueProps,
                    )
                }
            }

            is ProfileEventElm.Internal.ServerEvent.EventQueueUpdated -> {
                val queueProps = state.eventQueueData?.copy(lastEventId = event.eventId)
                state {
                    copy(
                        eventQueueData = queueProps,
                    )
                }
                commands {
                    +ProfileCommandElm.ObservePresence(
                        profileId = state.profileId,
                        isRestart = false,
                        queueProps = queueProps,
                    )
                }
            }

            is ProfileEventElm.Internal.ServerEvent.PresenceUpdated -> {
                val queueProps = state.eventQueueData?.copy(lastEventId = event.eventId)
                state {
                    copy(
                        eventQueueData = queueProps,
                        screenState = screenState.map { person ->
                            person.copy(
                                status = event.newStatus
                            )
                        }
                    )
                }
                commands {
                    +ProfileCommandElm.ObservePresence(
                        profileId = state.profileId,
                        isRestart = false,
                        queueProps = queueProps,
                    )
                }
            }
        }
    }

    override fun Result.ui(event: ProfileEventElm.Ui): Any =
        when(event) {
            ProfileEventElm.Ui.ClickedOnBack -> commands { +ProfileCommandElm.GoBack }
            ProfileEventElm.Ui.ClickedOnRetry -> commands { +ProfileCommandElm.LoadData(profileId = state.profileId) }
            ProfileEventElm.Ui.Init -> commands { +ProfileCommandElm.LoadData(profileId = state.profileId) }
        }

}