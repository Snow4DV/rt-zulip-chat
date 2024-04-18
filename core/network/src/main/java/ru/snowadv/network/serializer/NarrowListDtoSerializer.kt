package ru.snowadv.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.snowadv.network.model.NarrowRequestDto
import ru.snowadv.network.model.NarrowListRequestDto

object NarrowListDtoSerializer : KSerializer<NarrowListRequestDto> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("NarrowListDto") {
            element("narrows", ListSerializer(NarrowRequestDto.serializer()).descriptor)
        }

    override fun serialize(encoder: Encoder, value: NarrowListRequestDto) {
        encoder.encodeSerializableValue(ListSerializer(NarrowRequestDto.serializer()), value.narrows)
    }

    override fun deserialize(decoder: Decoder): NarrowListRequestDto {
        val narrows = decoder.decodeSerializableValue(ListSerializer(NarrowRequestDto.serializer()))
        return NarrowListRequestDto(narrows)
    }
}