package ru.snowadv.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.snowadv.network.model.NarrowRequestDto
import ru.snowadv.network.model.NarrowListRequestDto
import ru.snowadv.network.model.UnsubscribeStreamsListRequestDto

internal object UnsubscribeStreamsListRequestDtoSerializer : KSerializer<UnsubscribeStreamsListRequestDto> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("UnsubscribeStreamsListRequestDto") {
            element("streams", ListSerializer(String.serializer()).descriptor)
        }

    override fun serialize(encoder: Encoder, value: UnsubscribeStreamsListRequestDto) {
        encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.streamNames)
    }

    override fun deserialize(decoder: Decoder): UnsubscribeStreamsListRequestDto {
        val streamNames = decoder.decodeSerializableValue(ListSerializer(String.serializer()))
        return UnsubscribeStreamsListRequestDto(streamNames)
    }
}