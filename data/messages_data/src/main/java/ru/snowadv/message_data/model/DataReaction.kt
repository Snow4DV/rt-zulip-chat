package ru.snowadv.message_data.model

data class DataReaction(
    val emojiName: String,
    val emojiCode: String,
    val reactionType: String,
    val count: Int,
    val userReacted: Boolean,
)