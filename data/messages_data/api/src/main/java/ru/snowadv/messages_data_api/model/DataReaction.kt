package ru.snowadv.messages_data_api.model

data class DataReaction(
    val emojiName: String,
    val emojiCode: String,
    val count: Int,
    val userReacted: Boolean,
)