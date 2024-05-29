package ru.snowadv.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.snowadv.network.serializer.NarrowListDtoSerializer
import ru.snowadv.network.serializer.SubscriptionDetailsListDtoSerializer

@Serializable(SubscriptionDetailsListDtoSerializer::class)
data class SubscriptionDetailsListRequestDto(
    val subscriptions: List<SubscriptionDetailsRequestDto>,
) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}