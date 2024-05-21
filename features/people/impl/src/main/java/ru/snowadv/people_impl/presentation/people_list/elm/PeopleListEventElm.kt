package ru.snowadv.people_impl.presentation.people_list.elm

import ru.snowadv.events_api.domain.model.EventInfoHolder
import ru.snowadv.events_api.domain.model.EventSenderType
import ru.snowadv.people_impl.presentation.model.Person

internal sealed interface PeopleListEventElm {

    sealed interface Ui : PeopleListEventElm {
        data object Init : Ui
        data object Paused : Ui
        data object Resumed : Ui
        data object ClickedOnRetry : Ui
        data object ClickedOnSearchIcon : Ui
        data class ClickedOnPerson(val userId: Long) : Ui
        data class ChangedSearchQuery(val query: String) : Ui
    }

    sealed interface Internal : PeopleListEventElm {
        data class PeopleLoaded(val people: List<Person>, val cached: Boolean) : Internal
        data class Error(val throwable: Throwable, val cachedPeople: List<Person>?) : Internal
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
                val userId: Long,
            ) :
                ServerEvent()

            data class EventQueueUpdated(override val queueId: String?, override val eventId: Long) :
                ServerEvent()
        }

    }
}