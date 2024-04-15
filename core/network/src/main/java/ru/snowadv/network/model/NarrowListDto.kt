package ru.snowadv.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.snowadv.network.serializer.NarrowListDtoSerializer

@Serializable(NarrowListDtoSerializer::class)
data class NarrowListDto(
    val narrows: List<NarrowDto>
) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}