package ru.snowadv.chat.domain.model

import ru.snowadv.chat.domain.model.Emoji
import ru.snowadv.chat.presentation.model.ChatEmoji

internal data class ChatReaction(val name: String, val code: Int, val count: Int, val userReacted: Boolean)