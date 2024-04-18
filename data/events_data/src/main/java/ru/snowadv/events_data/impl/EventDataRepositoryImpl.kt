package ru.snowadv.events_data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import ru.snowadv.events_data.api.EventDataRepository
import ru.snowadv.events_data.model.DataEvent
import ru.snowadv.events_data.model.DataEventType
import ru.snowadv.events_data.model.DataNarrow
import ru.snowadv.events_data.util.toDataEvent
import ru.snowadv.events_data.util.toDto
import ru.snowadv.events_data.util.toStringEventTypes
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.stub.StubZulipApi

class EventDataRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
): EventDataRepository {
    private val api: ZulipApi = StubZulipApi
    override fun listenToEvents(
        types: List<DataEventType>,
        narrows: List<DataNarrow>
    ): Flow<Resource<List<DataEvent>>> = flow {
        var initEventId: Long
        val queueId = api.registerEventQueue(types.toStringEventTypes(), narrows.map { it.toDto().toNarrowList() }).onFailure {
            emit(Resource.Error(it))
            return@flow
        }.getOrThrow().also { initEventId = it.lastEventId }.queueId

        obtainEventQueueEndlessly(queueId, initEventId).collect {
            emit(it)
        }
    }.flowOn(ioDispatcher)

    private fun obtainEventQueueEndlessly(
        queueId: String,
        initEventId: Long,
    ): Flow<Resource<List<DataEvent>>> = flow {
        var lastEventId = initEventId

        while (currentCoroutineContext().isActive) {
            api.getEventsFromEventQueue(queueId, lastEventId).fold(
                onSuccess = { eventsDto ->
                    emit(Resource.Success(eventsDto.events.map { it.toDataEvent() }))
                    eventsDto.events.lastOrNull()?.let { lastEventDto ->
                        lastEventId = lastEventDto.id
                    }
                },
                onFailure = {
                    emit(Resource.Error(it))
                    return@flow
                },
            )
        }
    }.flowOn(ioDispatcher)
}