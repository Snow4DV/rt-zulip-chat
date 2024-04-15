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
import ru.snowadv.network.model.Narrow2DArrayDto
import ru.snowadv.network.model.NarrowDto
import ru.snowadv.network.model.NarrowListDto

object Narrow2DArrayDtoSerializer : KSerializer<Narrow2DArrayDto> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Narrow2DArrayDto") {
            element("narrows", ListSerializer(NarrowDto.serializer()).descriptor)
        }

    override fun serialize(encoder: Encoder, value: Narrow2DArrayDto) {
        encoder.encodeSerializableValue(
            ListSerializer(ListSerializer(String.serializer())),
            value.narrows.map { listOf(it.operator, it.operand) }
        )
    }

    override fun deserialize(decoder: Decoder): Narrow2DArrayDto {
        val narrows =
            decoder.decodeSerializableValue(ListSerializer(ListSerializer(String.serializer())))
        return Narrow2DArrayDto(narrows.map { NarrowDto(it[0], it[1]) })
    }
}