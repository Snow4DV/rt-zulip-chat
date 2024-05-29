package ru.snowadv.chatapp.data

import kotlinx.serialization.json.Json
import ru.snowadv.auth_storage.model.StorageAuthUser
import ru.snowadv.chatapp.util.AssetsUtils.fromAssets
import ru.snowadv.network.model.AllStreamsResponseDto
import ru.snowadv.network.model.AllUsersResponseDto
import ru.snowadv.network.model.MessagesResponseDto
import ru.snowadv.network.model.SingleUserResponseDto
import ru.snowadv.network.model.SubscribedStreamsResponseDto
import ru.snowadv.network.model.TopicsResponseDto
import ru.snowadv.network_authorizer.model.AuthResponseDto
import javax.inject.Inject

internal class MockData @Inject constructor(private val json: Json){
    val streamsDto: AllStreamsResponseDto by lazy { json.decodeFromString(fromAssets("channels/streams.json")) }
    val topicsDto: TopicsResponseDto by lazy { json.decodeFromString(fromAssets("channels/topics.json")) }
    val subscriptionsDto: SubscribedStreamsResponseDto by lazy { json.decodeFromString(fromAssets("channels/subscriptions.json")) }
    val messagesDto: MessagesResponseDto by lazy { json.decodeFromString(fromAssets("chat/messages.json")) }
    val peopleDto: AllUsersResponseDto by lazy { json.decodeFromString(fromAssets("people/people.json")) }
    val profile: SingleUserResponseDto by lazy { json.decodeFromString(fromAssets("profile/profile.json")) }
    val auth: AuthResponseDto by lazy { json.decodeFromString(fromAssets("auth/api_key.json")) }
    val user = StorageAuthUser(
        id = auth.userId,
        email = auth.email,
        apiKey = auth.apiKey,
    )
    val userName = "Mikhail Loginov"
    val testPassword = "testpassword"
}