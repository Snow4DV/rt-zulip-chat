package ru.snowadv.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.snowadv.network.serializer.Narrow2DArrayDtoSerializer
import ru.snowadv.network.serializer.NarrowListDtoSerializer

@Serializable(with = Narrow2DArrayDtoSerializer::class)
data class Narrow2DArrayDto(
    val narrows: List<NarrowDto>
) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}