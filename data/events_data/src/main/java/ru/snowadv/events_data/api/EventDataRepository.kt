package ru.snowadv.events_data.api

import kotlinx.coroutines.flow.Flow
import ru.snowadv.events_data.model.DataEvent
import ru.snowadv.events_data.model.DataEventType
import ru.snowadv.events_data.model.DataNarrow
import ru.snowadv.model.Resource

interface EventDataRepository {

    fun listenToEvents(types: List<DataEventType>, narrows: List<DataNarrow>): Flow<Resource<List<DataEvent>>>
}