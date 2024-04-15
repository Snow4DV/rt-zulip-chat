package ru.snowadv.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.snowadv.network.serializer.EventTypesDtoSerializer
import ru.snowadv.network.serializer.Narrow2DArrayDtoSerializer
import ru.snowadv.network.serializer.NarrowListDtoSerializer

@Serializable(with = EventTypesDtoSerializer::class)
data class EventTypesDto(
    val eventTypes: List<String>,
) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}