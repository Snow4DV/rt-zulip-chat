package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionResponseDto(
    @SerialName("user_id")
    val userId: Long,
    @SerialName("emoji_name")
    val emojiName: String,
    @SerialName("emoji_code")
    val emojiCode: String,
    @SerialName("reaction_type")
    val reactionType: String,
)