package ru.snowadv.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.snowadv.network.serializer.ChangeFlagsMessagesIdsListRequestDtoSerializer
import ru.snowadv.network.serializer.UnsubscribeStreamsListRequestDtoSerializer

@Serializable(ChangeFlagsMessagesIdsListRequestDtoSerializer::class)
data class ChangeFlagsMessagesIdsListRequestDto(
    val messageIds: List<Long>
) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}