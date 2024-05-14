package ru.snowadv.profile_impl.presentation.profile.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.snowadv.events_api.domain.model.DomainEvent
import ru.snowadv.model.Resource
import ru.snowadv.profile_api.domain.navigation.ProfileRouter
import ru.snowadv.profile_impl.domain.use_case.GetProfileUseCase
import ru.snowadv.profile_impl.domain.use_case.ListenToPresenceEventsUseCase
import ru.snowadv.profile_impl.presentation.util.ProfileMappers.toElmEvent
import ru.snowadv.profile_impl.presentation.util.ProfileMappers.toUiModel
import ru.snowadv.profile_impl.presentation.util.ProfileMappers.toUpdateQueueDataElmEvent
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

@Reusable
internal class ProfileActorElm @Inject constructor( // actor that interacts with domain layer
    private val router: ProfileRouter,
    private val getProfileUseCase: GetProfileUseCase,
    private val listenToPresenceEventsUseCase: ListenToPresenceEventsUseCase,
): Actor<ProfileCommandElm, ProfileEventElm>() {
    override fun execute(command: ProfileCommandElm): Flow<ProfileEventElm> = when(command) {
        is ProfileCommandElm.LoadData -> {
            getProfileUseCase.invoke(command.profileId).map { res ->
                when(res) {
                    is Resource.Error -> ProfileEventElm.Internal.Error(res.throwable)
                    is Resource.Loading -> ProfileEventElm.Internal.Loading
                    is Resource.Success -> ProfileEventElm.Internal.PersonLoaded(res.data.toUiModel())
                }
            }
        }
        is ProfileCommandElm.ObservePresence -> {
            listenToPresenceEventsUseCase.invoke(command.profileId, command.isRestart, command.queueProps).map { event ->
                when(event) {
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
    }

}