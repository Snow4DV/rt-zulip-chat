package ru.snowadv.profile_impl.presentation.profile.elm

import ru.snowadv.events_api.helper.StateMachineQueueHelper
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.ScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

internal class ProfileReducerElm @Inject constructor()  : ScreenDslReducer<ProfileEventElm, ProfileEventElm.Ui, ProfileEventElm.Internal, ProfileStateElm, ProfileEffectElm, ProfileCommandElm>(
    uiEventClass = ProfileEventElm.Ui::class,
    internalEventClass = ProfileEventElm.Internal::class,
) {
    override fun Result.internal(event: ProfileEventElm.Internal) {
        if (!StateMachineQueueHelper.determineIfEventIsByServerAndBelongsToStateOrOther(state.eventQueueData, event)) return
        when (event) {
            is ProfileEventElm.Internal.Error -> state {
                copy(
                    screenState = ScreenState.Error(event.throwable, event.cachedPerson),
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

                if (event.cached) return

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
                    screenState = ScreenState.Loading(),
                    eventQueueData = null,
                )
            }

            is ProfileEventElm.Internal.ServerEvent.EventQueueFailed -> {
                if (event.recreateQueue) {
                    state {
                        copy(eventQueueData = null)
                    }
                    commands {
                        +ProfileCommandElm.LoadData(state.profileId)
                    }
                } else if (state.isResumed) {
                    commands {
                        +ProfileCommandElm.ObservePresence(
                            profileId = state.profileId,
                            isRestart = true,
                            queueProps = state.eventQueueData,
                        )
                    }
                }
            }

            is ProfileEventElm.Internal.ServerEvent.EventQueueRegistered -> {
                state {
                    copy(
                        eventQueueData = EventQueueProperties(
                            event.queueId,
                            event.timeoutSeconds,
                            event.eventId
                        ),
                    )
                }
                observeCommand()
            }

            is ProfileEventElm.Internal.ServerEvent.EventQueueUpdated -> {
                state {
                    copy(
                        eventQueueData = state.eventQueueData?.copy(lastEventId = event.eventId),
                    )
                }
                observeCommand()
            }

            is ProfileEventElm.Internal.ServerEvent.PresenceUpdated -> {
                state {
                    copy(
                        eventQueueData = state.eventQueueData?.copy(lastEventId = event.eventId),
                        screenState = screenState.map { person ->
                            person.copy(
                                status = event.newStatus
                            )
                        }
                    )
                }
                observeCommand()
            }
        }
    }

    override fun Result.ui(event: ProfileEventElm.Ui): Any =
        when(event) {
            ProfileEventElm.Ui.ClickedOnBack -> commands { +ProfileCommandElm.GoBack }
            ProfileEventElm.Ui.ClickedOnRetry -> commands { +ProfileCommandElm.LoadData(profileId = state.profileId) }
            ProfileEventElm.Ui.Init -> commands { +ProfileCommandElm.LoadData(profileId = state.profileId) }
            ProfileEventElm.Ui.Paused -> state {
                copy(isResumed = false)
            }
            ProfileEventElm.Ui.Resumed -> {
                state {
                    copy(isResumed = true)
                }
                observeCommand()
            }

            ProfileEventElm.Ui.ClickedOnLogout -> commands {
                +ProfileCommandElm.Logout
            }
        }

    private fun Result.observeCommand() {
        if (!state.isResumed || state.screenState !is ScreenState.Success) return
        commands {
            +ProfileCommandElm.ObservePresence(
                profileId = state.profileId,
                isRestart = false,
                queueProps = state.eventQueueData,
            )
        }
    }

}