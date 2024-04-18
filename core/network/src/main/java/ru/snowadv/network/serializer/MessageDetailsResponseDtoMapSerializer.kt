package ru.snowadv.network.serializer

import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import ru.snowadv.network.model.MessageDetailsResponseDto

object MessageDetailsResponseDtoMapSerializer : JsonTransformingSerializer<Map<String, MessageDetailsResponseDto>>(
    MapSerializer(String.serializer(), MessageDetailsResponseDto.serializer())
) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val jsonObject = element.jsonObject
        val resultMap = HashMap<String, MessageDetailsResponseDto>()
        for ((key, value) in jsonObject) {
            val messageDetail = Json.decodeFromJsonElement<MessageDetailsResponseDto>(value)
            resultMap[key] = messageDetail
        }
        return Json.encodeToJsonElement(resultMap)
    }
}
