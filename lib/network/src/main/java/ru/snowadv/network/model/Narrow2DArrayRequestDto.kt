package ru.snowadv.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.snowadv.network.serializer.Narrow2DArrayDtoSerializer

@Serializable(with = Narrow2DArrayDtoSerializer::class)
data class Narrow2DArrayRequestDto(
    val narrows: List<NarrowRequestDto>
) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}