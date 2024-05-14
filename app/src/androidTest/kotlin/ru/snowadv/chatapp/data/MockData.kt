package ru.snowadv.chatapp.data

import kotlinx.serialization.json.Json
import ru.snowadv.chatapp.util.AssetsUtils.fromAssets
import ru.snowadv.network.model.MessagesResponseDto

internal object MockData {
    private val json = Json { ignoreUnknownKeys = true }
    val initialMessages: MessagesResponseDto = json.decodeFromString(fromAssets("chat/messages.json"))
    val people: MessagesResponseDto = json.decodeFromString(fromAssets("chat/people.json"))
}