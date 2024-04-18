package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessagesResponseDto(
    @SerialName("messages")
    val messages: List<MessageResponseDto>,
    @SerialName("found_anchor")
    val foundAnchor: Boolean,
    @SerialName("found_oldest")
    val foundOldest: Boolean,
    @SerialName("found_newest")
    val foundNewest: Boolean,
)

@Serializable
data class MessageResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("content")
    val content: String,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("sender_id")
    val senderId: Long,
    @SerialName("sender_full_name")
    val senderFullName: String,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("reactions")
    val reactions: List<ReactionResponseDto>,
    @SerialName("type")
    val type: String,
    @SerialName("subject")
    val subject: String, // will be empty string for DM messages
    @SerialName("stream_id")
    val streamId: Long? = null, // will be null if it is DM message
)

/*
"stream_id": 432915,
"type": "stream",
"subject": "testing",
 */