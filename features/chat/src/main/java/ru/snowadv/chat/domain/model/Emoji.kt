package ru.snowadv.chat.domain.model

internal data class  Emoji(
    val name: String,
    val code: Int,
) {
    fun getCodeString() = String(Character.toChars(code))
}