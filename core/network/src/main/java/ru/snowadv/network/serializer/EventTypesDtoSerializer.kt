package ru.snowadv.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.snowadv.network.model.EventTypesRequestDto

object EventTypesDtoSerializer : KSerializer<EventTypesRequestDto> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("EventTypesDto") {
            element("eventTypes", ListSerializer(String.serializer()).descriptor)
        }

    override fun serialize(encoder: Encoder, value: EventTypesRequestDto) {
        encoder.encodeSerializableValue(
            ListSerializer(String.serializer()),
            value.eventTypes
        )
    }

    override fun deserialize(decoder: Decoder): EventTypesRequestDto {
        val eventTypes =
            decoder.decodeSerializableValue(ListSerializer(String.serializer()))
        return EventTypesRequestDto(eventTypes)
    }
}