package ru.snowadv.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.buildJsonObject
import ru.snowadv.network.model.EventTypesDto
import ru.snowadv.network.model.Narrow2DArrayDto
import ru.snowadv.network.model.NarrowDto
import ru.snowadv.network.model.NarrowListDto

object EventTypesDtoSerializer : KSerializer<EventTypesDto> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("EventTypesDto") {
            element("eventTypes", ListSerializer(String.serializer()).descriptor)
        }

    override fun serialize(encoder: Encoder, value: EventTypesDto) {
        encoder.encodeSerializableValue(
            ListSerializer(String.serializer()),
            value.eventTypes
        )
    }

    override fun deserialize(decoder: Decoder): EventTypesDto {
        val eventTypes =
            decoder.decodeSerializableValue(ListSerializer(String.serializer()))
        return EventTypesDto(eventTypes)
    }
}