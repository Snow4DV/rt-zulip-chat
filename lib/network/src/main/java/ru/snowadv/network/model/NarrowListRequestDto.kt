package ru.snowadv.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.snowadv.network.serializer.NarrowListDtoSerializer

@Serializable(NarrowListDtoSerializer::class)
data class NarrowListRequestDto(
    val narrows: List<NarrowRequestDto>
) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}