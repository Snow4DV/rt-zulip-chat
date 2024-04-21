package ru.snowadv.event_api.helper

import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.event_api.model.EventSenderType

object StateMachineQueueHelper {
    fun determineIfServerEventBelongsToStateQueue(
        queueProps: EventQueueProperties?,
        event: DomainEvent
    ): Boolean {
        return when (event) {
            is DomainEvent.FailedFetchingQueueEvent -> queueProps?.queueId == event.queueId
            is DomainEvent.RegisteredNewQueueEvent -> queueProps == null
            else -> queueProps != null && queueProps.queueId == event.queueId && queueProps.lastEventId < event.id
        }
    }

    fun determineIfServerEventBelongsToStateQueue(
        queueProps: EventQueueProperties?,
        queueId: String?,
        eventId: Long
    ): Boolean {
        return queueProps != null && queueProps.queueId == queueId && queueProps.lastEventId < eventId
    }

    fun determineIfServerEventBelongsToStateQueue(
        queueProps: EventQueueProperties?,
        eventInfoHolder: EventInfoHolder,
    ): Boolean {
        return when(eventInfoHolder.senderType) {
            EventSenderType.SERVER_SIDE -> queueProps != null && queueProps.queueId == eventInfoHolder.queueId
                    && queueProps.lastEventId < eventInfoHolder.eventId
            EventSenderType.SYNTHETIC_FAIL -> queueProps != null && queueProps.queueId == eventInfoHolder.queueId
            EventSenderType.SYNTHETIC_REGISTER -> queueProps == null
        }
    }

    fun determineIfEventIsByServerAndBelongsToStateOrOther(
        queueProps: EventQueueProperties?,
        event: Any,
    ): Boolean {
        return if (event is EventInfoHolder) {
            determineIfServerEventBelongsToStateQueue(queueProps, event)
        } else {
            true
        }
    }
}