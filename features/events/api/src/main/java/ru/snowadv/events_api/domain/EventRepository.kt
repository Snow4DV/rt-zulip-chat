package ru.snowadv.events_api.domain

import kotlinx.coroutines.flow.Flow
import ru.snowadv.events_api.domain.model.DomainEvent
import ru.snowadv.events_api.domain.model.EventNarrow
import ru.snowadv.events_api.domain.model.EventQueueProperties
import ru.snowadv.events_api.domain.model.EventType

interface EventRepository {
    /**
     * Here's how it works:
     * 1) You pass EventQueueListenerBag that SHOULD be stored in an object that WILL OUTLIVE
     * the listener flow (for example: in screen's view model)
     * 2) Flow can be restarted as many times as needed (with screen's lifecycle). When flow
     * starts, it will look inside the bag and check if there's a queue id already. If so - it will
     * try to start collecting events with lastEventId and queueId that are stored in the bag.
     * If there's none - it will register new queue.
     *
     * Why is it needed? -> Because we do not listen to a flow while app is stopped (in onStop). If we happen to
     * loose some events in the mean time and start listening with a new queue, we will never get these
     * events and user will see outdated data.
     *
     * With this solution, if 10 minutes since last event didn't pass, (event queue time-to-live)
     * we will catch up with missed events pretty easily using existing queueId and lastEventId.
     *
     * BUT if 10 minutes have passed, screen can't be recovered and will have to reinitialize.
     * (see https://zulip.com/api/register-queue - BAD_EVENT_QUEUE_ID, there's no other way)
     * In that case, reloadAction suspend function will be called. We will wait until it finishes.
     * If it succeeds - we can start collecting new events, otherwise we can't do so.
     *
     * P.S. Restarting mechanism also exists in the implementation: if we happen to be unable to obtain new events,
     * queue will try restarting every 5 or so seconds without bothering user.
     *
     * P.P.S. This flow should only be collected when screen is fully loaded an initialized.
     * I recommend stopping it when screen state goes to Error/Loading and starting it when it goes back to Success/Empty
     *
     * Refer to MutableEventQueueListenerBag to see how it works - nothing special about it,
     * it simply holds mutable queueId and lastEventId.
     */

    fun listenEvents(
        types: Set<EventType>,
        narrows: Set<EventNarrow>,
        eventQueueProps: EventQueueProperties?,
        delayBeforeObtain: Boolean = false,
    ): Flow<DomainEvent>
}