package ru.snowadv.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.buildJsonObject
import ru.snowadv.network.model.NarrowDto
import ru.snowadv.network.model.NarrowListDto

object NarrowListDtoSerializer : KSerializer<NarrowListDto> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("NarrowListDto") {
            element("narrows", ListSerializer(NarrowDto.serializer()).descriptor)
        }

    override fun serialize(encoder: Encoder, value: NarrowListDto) {
        encoder.encodeSerializableValue(ListSerializer(NarrowDto.serializer()), value.narrows)
    }

    override fun deserialize(decoder: Decoder): NarrowListDto {
        val narrows = decoder.decodeSerializableValue(ListSerializer(NarrowDto.serializer()))
        return NarrowListDto(narrows)
    }
}