package ru.snowadv.chat.presentation.model

import ru.snowadv.presentation.adapter.DelegateItem

data class ChatEmoji(
    val name: String,
    val code: Int,
): DelegateItem {
    override val id: Any
        get() = code

    fun getCodeString() = String(Character.toChars(code))
}