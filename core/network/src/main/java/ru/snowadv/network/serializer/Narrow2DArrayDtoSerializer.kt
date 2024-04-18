package ru.snowadv.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.snowadv.network.model.Narrow2DArrayRequestDto
import ru.snowadv.network.model.NarrowRequestDto

object Narrow2DArrayDtoSerializer : KSerializer<Narrow2DArrayRequestDto> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Narrow2DArrayDto") {
            element("narrows", ListSerializer(NarrowRequestDto.serializer()).descriptor)
        }

    override fun serialize(encoder: Encoder, value: Narrow2DArrayRequestDto) {
        encoder.encodeSerializableValue(
            ListSerializer(ListSerializer(String.serializer())),
            value.narrows.map { listOf(it.operator, it.operand) }
        )
    }

    override fun deserialize(decoder: Decoder): Narrow2DArrayRequestDto {
        val narrows =
            decoder.decodeSerializableValue(ListSerializer(ListSerializer(String.serializer())))
        return Narrow2DArrayRequestDto(narrows.map { NarrowRequestDto(it[0], it[1]) })
    }
}