package ru.snowadv.events_api.helper

import ru.snowadv.events_api.model.EventInfoHolder
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.events_api.model.EventSenderType

object StateMachineQueueHelper {
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

    private fun determineIfServerEventBelongsToStateQueue(
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
}