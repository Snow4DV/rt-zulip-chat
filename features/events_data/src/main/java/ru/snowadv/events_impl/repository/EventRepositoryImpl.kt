package ru.snowadv.events_impl.repository

import dagger.Reusable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.events_api.repository.EventRepository
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventNarrow
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.events_api.model.EventType
import ru.snowadv.events_impl.util.EventMapper.toDomainEvent
import ru.snowadv.events_impl.util.EventMapper.toEventTypesDto
import ru.snowadv.events_impl.util.EventMapper.toNarrow2DArrayDto
import ru.snowadv.events_impl.util.EventMapper.toRegisteredQueueEvent
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
        const val RETRY_DELAY_MILLIS = 2_000L
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
                    it.events.map { eventDto -> eventDto.toDomainEvent(userId, eventQueueProps.queueId) }.forEach { event ->
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
                                reason = it,
                            ))
                        }
                        else -> {
                            emit(
                                DomainEvent.FailedFetchingQueueEvent(
                                id = eventQueueProps.lastEventId,
                                queueId = eventQueueProps.queueId,
                                isQueueBad = false,
                                reason = it,
                            ))
                        }
                    }

                },
            )
        } else {
            api.registerEventQueue(
                eventTypes = (types + EventType.HEARTBEAT + EventType.REALM).toEventTypesDto(),
                narrow = narrows.toNarrow2DArrayDto(),
                applyMarkdown = true,
            ).getOrElse {
                emit(DomainEvent.FailedFetchingQueueEvent(-1, null, it, true))
                return@flow
            }.let { eventsDto ->
                emit(eventsDto.toRegisteredQueueEvent())
            }
        }
    }
}