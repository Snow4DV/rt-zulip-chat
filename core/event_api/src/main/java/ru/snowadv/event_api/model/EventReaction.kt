package ru.snowadv.event_api.model

data class EventReaction(
    val name: String,
    val emojiCode: String,
    val count: Int,
    val userReacted: Boolean,
    val reactionType: String,
)