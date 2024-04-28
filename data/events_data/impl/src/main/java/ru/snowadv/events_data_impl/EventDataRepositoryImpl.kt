package ru.snowadv.events_data_impl

import dagger.Reusable
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.isActive
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.event_api.helper.MutableEventQueueListenerBag
import ru.snowadv.events_data_api.model.DomainEvent
import ru.snowadv.events_data_api.model.EventNarrow
import ru.snowadv.event_api.helper.EventQueueProperties
import ru.snowadv.events_data_api.model.EventType
import ru.snowadv.events_data_api.EventRepository
import ru.snowadv.events_data_impl.exception.UnableToObtainQueueException
import ru.snowadv.events_data_impl.util.EventMapper.toDataEvent
import ru.snowadv.events_data_impl.util.EventMapper.toEventStreamMessages
import ru.snowadv.events_data_impl.util.EventMapper.toEventTypesDto
import ru.snowadv.events_data_impl.util.EventMapper.toNarrow2DArrayDto
import ru.snowadv.events_data_impl.util.EventMapper.toRegisteredQueueEvent
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.utils.NetworkUtils.getHttpExceptionCode
import java.io.IOException
import javax.inject.Inject

@Reusable
class EventDataRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val authProvider: AuthProvider,
    private val api: ZulipApi,
) : EventRepository {

    companion object {
        const val RETRY_DELAY_MILLIS = 10_000L
        const val BAD_EVENT_QUEUE_ID_ERROR_CODE = "BAD_EVENT_QUEUE_ID"
        const val BAD_REQUEST_ERROR_CODE = "BAD_REQUEST"
    }

    override fun listenForEvents( // TODO remove and move all screens to new event system
        bag: MutableEventQueueListenerBag,
        types: List<EventType>,
        narrows: List<EventNarrow>,
        reloadAction: suspend () -> Unit
    ): Flow<DomainEvent> = flow {
        // Start new queue in case it hasn't started already
        if (bag.queueId == null) {
            api.registerEventQueue(types.toEventTypesDto(), narrows.toNarrow2DArrayDto())
                .onFailure {
                    bag.clear()
                }.getOrThrow()
                .let { eventQueueDto -> // Will throw in case of failure to trigger restart later
                    bag.lastEventId = eventQueueDto.lastEventId
                    bag.queueId = eventQueueDto.queueId
                    bag.timeoutSeconds = eventQueueDto.longPollTimeoutSeconds

                    eventQueueDto.unreadMessages?.let { unreadMessagesDto -> // Present ONLY if message and update_message_flags are both present in types
                        emit(
                            DomainEvent.RegisteredNewQueueEvent(
                                id = bag.lastEventId,
                                timeoutSeconds = eventQueueDto.longPollTimeoutSeconds,
                                queueId = eventQueueDto.queueId,
                                streamUnreadMessages = unreadMessagesDto.streams.toEventStreamMessages(),
                            )
                        )
                    }
                }
            bag.lastEventId
        }

        // Listen to queue
        obtainEventQueueEndlessly(bag, queueId = bag.queueId ?: error("bad queue"))
            .collect { emit(it) }
    }.retryWhen { cause, _ ->
        bag.clear()
        if (cause is UnableToObtainQueueException) {
            reloadAction()
        }
        delay(RETRY_DELAY_MILLIS)
        true
    }.flowOn(dispatcherProvider.io)

    override fun listenEvents(
        types: Set<EventType>,
        narrows: Set<EventNarrow>,
        eventQueueProps: EventQueueProperties?,
        delayBeforeObtain: Boolean
    ): Flow<DomainEvent> = flow {
        if (delayBeforeObtain) {
            delay(RETRY_DELAY_MILLIS)
        }
        if (eventQueueProps !=  null) {
            api.getEventsFromEventQueue(
                queueId = eventQueueProps.queueId,
                lastEventId = eventQueueProps.lastEventId,
                readTimeout = eventQueueProps.timeoutSeconds * 1000L,
            ).fold(
                onSuccess = {
                    val userId = authProvider.getAuthorizedUser().id
                    it.events.map { eventDto -> eventDto.toDataEvent(userId, eventQueueProps.queueId) }.forEach { event ->
                        emit(event)
                    }
                },
                onFailure = {
                    when (it.getHttpExceptionCode()) {
                        BAD_REQUEST_ERROR_CODE -> {
                            return@flow // We do nothing in that case. Most likely queue was collected twice.
                        }
                        BAD_EVENT_QUEUE_ID_ERROR_CODE -> {
                            emit(
                                DomainEvent.FailedFetchingQueueEvent(
                                id = eventQueueProps.lastEventId,
                                queueId = eventQueueProps.queueId,
                                isQueueBad = true,
                            ))
                        }
                        else -> {
                            emit(
                                DomainEvent.FailedFetchingQueueEvent(
                                id = eventQueueProps.lastEventId,
                                queueId = eventQueueProps.queueId,
                                isQueueBad = false,
                            ))
                        }
                    }

                },
            )
        } else {
            api.registerEventQueue(
                eventTypes = (types + EventType.HEARTBEAT + EventType.REALM).toEventTypesDto(),
                narrow = narrows.toNarrow2DArrayDto(),
            ).getOrElse {
                emit(DomainEvent.FailedFetchingQueueEvent(-1, null, true))
                return@flow
            }.let { eventsDto ->
                emit(eventsDto.toRegisteredQueueEvent())
            }
        }
    }
    @Throws(IOException::class, IllegalStateException::class, UnableToObtainQueueException::class)
    private fun obtainEventQueueEndlessly(bag: MutableEventQueueListenerBag, queueId: String): Flow<DomainEvent> =
        flow {
            while (currentCoroutineContext().isActive) {
                api.getEventsFromEventQueue(
                    queueId = bag.queueId
                        ?: error("Mutable event bag changed concurrently. Failed to fetch new events"),
                    lastEventId = bag.lastEventId,
                    readTimeout = bag.timeoutSeconds * 1000L
                ).getOrElse { throw UnableToObtainQueueException() }
                    .let { eventsDto ->
                        eventsDto.events.map { it.toDataEvent(authProvider.getAuthorizedUser().id, queueId = queueId) }
                            .forEach { domainEvent ->
                                emit(domainEvent)
                            }
                        eventsDto.events.lastOrNull()?.let { lastEventDto ->
                            bag.lastEventId = lastEventDto.id
                        }
                    }
            }
        }.flowOn(dispatcherProvider.io)
}