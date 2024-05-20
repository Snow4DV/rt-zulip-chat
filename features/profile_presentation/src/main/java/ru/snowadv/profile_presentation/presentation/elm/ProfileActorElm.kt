package ru.snowadv.profile_presentation.presentation.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.model.Resource
import ru.snowadv.profile_presentation.ui.util.ProfileMappers.toElmEvent
import ru.snowadv.profile_presentation.ui.util.ProfileMappers.toUpdateQueueDataElmEvent
import ru.snowadv.profile_presentation.navigation.ProfileRouter
import ru.snowadv.users_domain_api.use_case.GetProfileUseCase
import ru.snowadv.users_domain_api.use_case.ListenToProfilePresenceEventsUseCase
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

@Reusable
internal class ProfileActorElm @Inject constructor(
    // actor that interacts with domain layer
    private val router: ProfileRouter,
    private val getProfileUseCase: GetProfileUseCase,
    private val listenToPresenceEventsUseCase: ListenToProfilePresenceEventsUseCase,
) : Actor<ProfileCommandElm, ProfileEventElm>() {
    override fun execute(command: ProfileCommandElm): Flow<ProfileEventElm> = when (command) {
        is ProfileCommandElm.LoadData -> {
            getProfileUseCase.invoke(command.profileId).map { res ->
                when (res) {
                    is Resource.Error -> ProfileEventElm.Internal.Error(
                        res.throwable,
                        res.data,
                    )

                    is Resource.Loading -> res.data?.let { data ->
                        ProfileEventElm.Internal.PersonLoaded(
                            person = data,
                            cached = true,
                        )
                    } ?: ProfileEventElm.Internal.Loading

                    is Resource.Success -> ProfileEventElm.Internal.PersonLoaded(
                        person = res.data,
                        cached = false,
                    )
                }
            }
        }

        is ProfileCommandElm.ObservePresence -> {
            listenToPresenceEventsUseCase.invoke(
                command.profileId,
                command.isRestart,
                command.queueProps
            ).map { event ->
                when (event) {
                    is DomainEvent.PresenceDomainEvent -> event.toElmEvent()
                    is DomainEvent.RegisteredNewQueueEvent -> event.toElmEvent()
                    is DomainEvent.FailedFetchingQueueEvent -> event.toElmEvent()
                    else -> event.toUpdateQueueDataElmEvent()
                }
            }
        }

        ProfileCommandElm.GoBack -> flow {
            router.goBack()
        }

        ProfileCommandElm.Logout -> flow {
            router.logOut()
        }
    }

}