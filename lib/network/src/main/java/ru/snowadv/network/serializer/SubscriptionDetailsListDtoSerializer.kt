package ru.snowadv.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.snowadv.network.model.NarrowRequestDto
import ru.snowadv.network.model.NarrowListRequestDto
import ru.snowadv.network.model.SubscriptionDetailsListRequestDto
import ru.snowadv.network.model.SubscriptionDetailsRequestDto

object SubscriptionDetailsListDtoSerializer : KSerializer<SubscriptionDetailsListRequestDto> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("SubscriptionDetailsListRequestDto") {
            element("subscriptions", ListSerializer(SubscriptionDetailsRequestDto.serializer()).descriptor)
        }

    override fun serialize(encoder: Encoder, value: SubscriptionDetailsListRequestDto) {
        encoder.encodeSerializableValue(ListSerializer(SubscriptionDetailsRequestDto.serializer()), value.subscriptions)
    }

    override fun deserialize(decoder: Decoder): SubscriptionDetailsListRequestDto {
        val subscriptionDetailsRequestDto = decoder.decodeSerializableValue(ListSerializer(SubscriptionDetailsRequestDto.serializer()))
        return SubscriptionDetailsListRequestDto(subscriptions = subscriptionDetailsRequestDto)
    }
}