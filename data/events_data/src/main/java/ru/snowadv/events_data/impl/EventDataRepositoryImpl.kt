package ru.snowadv.events_data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.isActive
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.event_api.helper.MutableEventQueueListenerBag
import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.event_api.model.EventNarrow
import ru.snowadv.event_api.model.EventStreamUpdateFlagsMessages
import ru.snowadv.event_api.model.EventType
import ru.snowadv.event_api.repository.EventRepository
import ru.snowadv.events_data.exception.UnableToObtainQueueException
import ru.snowadv.events_data.util.EventMapper.toDataEvent
import ru.snowadv.events_data.util.EventMapper.toEventTopicUnreadMessages
import ru.snowadv.events_data.util.EventMapper.toEventTypesDto
import ru.snowadv.events_data.util.EventMapper.toNarrow2DArrayDto
import ru.snowadv.network.api.ZulipApi
import java.io.IOException

class EventDataRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val authProvider: AuthProvider,
    private val api: ZulipApi,
) : EventRepository {

    companion object {
        const val RETRY_DELAY_MILLIS = 10_000L
    }

    override fun listenForEvents(
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
                            DomainEvent.UnreadMessagesEvent(
                                id = bag.lastEventId,
                                streamUnreadMessages = unreadMessagesDto.streams.groupBy { it.streamId }.map {
                                    EventStreamUpdateFlagsMessages(
                                        it.key,
                                        it.value.map { streamTopicUnreadMessagesDto -> streamTopicUnreadMessagesDto.toEventTopicUnreadMessages() })
                                }
                            )
                        )
                    }
                }
            bag.lastEventId
        }

        // Listen to queue
        obtainEventQueueEndlessly(bag)
            .collect { emit(it) }
    }.retryWhen { cause, _ ->
        bag.clear()
        if (cause is UnableToObtainQueueException) {
            reloadAction()
        }
        delay(RETRY_DELAY_MILLIS)
        true
    }.flowOn(ioDispatcher)

    @Throws(IOException::class, IllegalStateException::class, UnableToObtainQueueException::class)
    private fun obtainEventQueueEndlessly(bag: MutableEventQueueListenerBag): Flow<DomainEvent> =
        flow {
            while (currentCoroutineContext().isActive) {
                api.getEventsFromEventQueue(
                    queueId = bag.queueId
                        ?: error("Mutable event bag changed concurrently. Failed to fetch new events"),
                    lastEventId = bag.lastEventId,
                    readTimeout = bag.timeoutSeconds * 1000L
                ).getOrElse { throw UnableToObtainQueueException() }
                    .let { eventsDto ->
                        eventsDto.events.map { it.toDataEvent(authProvider.getAuthorizedUser().id) }
                            .forEach { domainEvent ->
                                emit(domainEvent)
                            }
                        eventsDto.events.lastOrNull()?.let { lastEventDto ->
                            bag.lastEventId = lastEventDto.id
                        }
                    }
            }
        }.flowOn(ioDispatcher)
}