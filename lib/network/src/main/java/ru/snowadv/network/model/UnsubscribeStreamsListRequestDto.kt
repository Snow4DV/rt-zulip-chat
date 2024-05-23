package ru.snowadv.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.snowadv.network.serializer.UnsubscribeStreamsListRequestDtoSerializer

@Serializable(UnsubscribeStreamsListRequestDtoSerializer::class)
data class UnsubscribeStreamsListRequestDto(
    val streamNames: List<String>
) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}