package ru.snowadv.database.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionEntity(
    @SerialName("user_id")
    val userId: Long,
    @SerialName("emoji_name")
    val emojiName: String,
    @SerialName("emoji_code")
    val emojiCode: String,
)