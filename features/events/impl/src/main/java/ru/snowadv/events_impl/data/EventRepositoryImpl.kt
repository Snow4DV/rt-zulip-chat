package ru.snowadv.events_impl.data

import dagger.Reusable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.auth_data_api.AuthProvider
import ru.snowadv.events_api.domain.EventRepository
import ru.snowadv.events_api.domain.model.DomainEvent
import ru.snowadv.events_api.domain.model.EventNarrow
import ru.snowadv.events_api.domain.model.EventQueueProperties
import ru.snowadv.events_api.domain.model.EventType
import ru.snowadv.events_impl.data.util.EventMapper.toDataEvent
import ru.snowadv.events_impl.data.util.EventMapper.toEventTypesDto
import ru.snowadv.events_impl.data.util.EventMapper.toNarrow2DArrayDto
import ru.snowadv.events_impl.data.util.EventMapper.toRegisteredQueueEvent
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.utils.NetworkUtils.getHttpExceptionCode
import javax.inject.Inject

@Reusable
class EventRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val authProvider: AuthProvider,
    private val api: ZulipApi,
) : EventRepository {

    companion object {
        const val RETRY_DELAY_MILLIS = 10_000L
        const val BAD_EVENT_QUEUE_ID_ERROR_CODE = "BAD_EVENT_QUEUE_ID"
        const val BAD_REQUEST_ERROR_CODE = "BAD_REQUEST"
    }



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
}