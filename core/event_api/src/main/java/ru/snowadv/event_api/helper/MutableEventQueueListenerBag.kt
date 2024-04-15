package ru.snowadv.event_api.helper

/**
 * This helper class is meant to be used by feature viewmodels or other state holders
 * to make it easier implementing event listener.
 *
 * Here's how it should be used:
 * 1) create a bag
 * 2) provide that bag to a flow that will look in it and check if the queue is already registered
 * (if so, the flow will just restart listening existing queue from the same place)
 *
 * If some error happen - flow shouldn't restart. It should just recreate a queue
 * and start listening to it again.
 *
 * Make note that there is an edge case: if app is in background, events appear and queue dies,
 * after restarting a queue you won't get events that occurred before queue death. In that case,
 * screen should reload!
 *
 * For better understating, refer to EventRepository docs
 *
 *
 * p.s. This bag is single-user only!
 */
class MutableEventQueueListenerBag(
    var queueId: String? = null,
    var lastEventId: Long = -1,
    var timeoutSeconds: Int = DEFAULT_TIMEOUT_SECONDS,
) {
    companion object {
        const val DEFAULT_TIMEOUT_SECONDS = 300
    }

    fun clear() {
        queueId = null
        lastEventId = -1
        timeoutSeconds = MutableEventQueueListenerBag.DEFAULT_TIMEOUT_SECONDS
    }
}