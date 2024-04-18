package ru.snowadv.chat.domain.model

data class  ChatEmoji(
    val name: String,
    val code: Int,
) {
    fun getCodeString() = String(Character.toChars(code))
}