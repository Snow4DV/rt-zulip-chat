package ru.snowadv.profile_impl.presentation.profile.elm

import ru.snowadv.events_api.model.EventInfoHolder
import ru.snowadv.events_api.model.EventSenderType
import ru.snowadv.profile_impl.presentation.model.Person

sealed interface ProfileEventElm {

    sealed interface Ui : ProfileEventElm {
        data object Init : Ui
        data object Paused : Ui
        data object Resumed : Ui
        data object ClickedOnRetry : Ui
        data object ClickedOnBack : Ui
        data object ClickedOnLogout : Ui
    }

    sealed interface Internal : ProfileEventElm {
        data class PersonLoaded(val person: Person, val cached: Boolean) : Internal
        data class Error(val throwable: Throwable, val cachedPerson: Person?) : Internal
        data object Loading : Internal

        sealed class ServerEvent : Internal, EventInfoHolder {
            data class EventQueueRegistered(
                override val queueId: String,
                override val eventId: Long,
                val timeoutSeconds: Int,
            ) : ServerEvent() {
                override val senderType: EventSenderType
                    get() = EventSenderType.SYNTHETIC_REGISTER
            }

            data class EventQueueFailed(
                override val queueId: String?,
                override val eventId: Long,
                val recreateQueue: Boolean,
            ) :
                ServerEvent() {
                override val senderType: EventSenderType
                    get() = EventSenderType.SYNTHETIC_FAIL
                }

            data class PresenceUpdated(
                override val queueId: String,
                override val eventId: Long,
                val newStatus: Person.Status,
            ) :
                ServerEvent()

            data class EventQueueUpdated(override val queueId: String?, override val eventId: Long) :
                ServerEvent()
        }

    }



}