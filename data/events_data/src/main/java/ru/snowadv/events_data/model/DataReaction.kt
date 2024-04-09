package ru.snowadv.events_data.model

data class DataReaction(
    val userId: Long,
    val emojiName: String,
    val emojiCode: Int,
    val reactionType: String,
)