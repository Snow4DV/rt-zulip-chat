package ru.snowadv.message_data.model

data class DataReaction(
    val userId: Long,
    val emojiName: String,
    val emojiCode: Int,
    val reactionType: String,
)