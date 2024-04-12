package ru.snowadv.events_data.model

data class DataReaction(
    val emojiName: String,
    val emojiCode: Int,
    val reactionType: String,
    val count: Int,
    val userReacted: Boolean,
)