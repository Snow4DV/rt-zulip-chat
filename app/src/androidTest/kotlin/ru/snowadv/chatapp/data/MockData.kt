package ru.snowadv.chatapp.data

import kotlinx.serialization.json.Json
import ru.snowadv.chatapp.util.AssetsUtils.fromAssets
import ru.snowadv.network.model.AllStreamsResponseDto
import ru.snowadv.network.model.AllUsersResponseDto
import ru.snowadv.network.model.MessagesResponseDto
import ru.snowadv.network.model.SingleUserResponseDto
import ru.snowadv.network.model.SubscribedStreamsResponseDto
import ru.snowadv.network.model.TopicsResponseDto

internal object MockData {
    private val json by lazy { Json { ignoreUnknownKeys = true } }

    val streams: AllStreamsResponseDto by lazy { json.decodeFromString(fromAssets("channels/streams.json")) }
    val topics: TopicsResponseDto by lazy { json.decodeFromString(fromAssets("channels/topics.json")) }
    val subscriptions: SubscribedStreamsResponseDto by lazy { json.decodeFromString(fromAssets("channels/subscriptions.json")) }
    val messages: MessagesResponseDto by lazy { json.decodeFromString(fromAssets("chat/messages.json")) }
    val people: AllUsersResponseDto by lazy { json.decodeFromString(fromAssets("people/people.json")) }
    val profile: SingleUserResponseDto by lazy { json.decodeFromString(fromAssets("profile/profile.json")) }
}