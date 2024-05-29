package ru.snowadv.network.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.snowadv.network.model.ChangeFlagsMessagesIdsListRequestDto
import ru.snowadv.network.model.NarrowRequestDto
import ru.snowadv.network.model.NarrowListRequestDto
import ru.snowadv.network.model.UnsubscribeStreamsListRequestDto

internal object ChangeFlagsMessagesIdsListRequestDtoSerializer : KSerializer<ChangeFlagsMessagesIdsListRequestDto> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ChangeFlagsMessagesIdsListRequestDto") {
            element("messageIds", ListSerializer(Long.serializer()).descriptor)
        }

    override fun serialize(encoder: Encoder, value: ChangeFlagsMessagesIdsListRequestDto) {
        encoder.encodeSerializableValue(ListSerializer(Long.serializer()), value.messageIds)
    }

    override fun deserialize(decoder: Decoder): ChangeFlagsMessagesIdsListRequestDto {
        val messageIds = decoder.decodeSerializableValue(ListSerializer(Long.serializer()))
        return ChangeFlagsMessagesIdsListRequestDto(messageIds)
    }
}