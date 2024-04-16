package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessagesDto(
    @SerialName("messages")
    val messages: List<MessageDto>,
    @SerialName("found_anchor")
    val foundAnchor: Boolean,
    @SerialName("found_oldest")
    val foundOldest: Boolean,
    @SerialName("found_newest")
    val foundNewest: Boolean,
)

@Serializable
data class MessageDto(
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
    val reactions: List<ReactionDto>,
)