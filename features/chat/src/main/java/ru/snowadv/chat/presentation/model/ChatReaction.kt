package ru.snowadv.chat.presentation.model

import ru.snowadv.chat.domain.model.Emoji

internal data class ChatReaction (val emoji: Emoji, val count: Int, val userReacted: Boolean)