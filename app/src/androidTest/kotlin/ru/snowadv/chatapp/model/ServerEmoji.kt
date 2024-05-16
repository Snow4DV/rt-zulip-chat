package ru.snowadv.chatapp.model

internal data class ServerEmoji(
    val name: String,
    val code: String
) {
    fun getCodeString() = String(Character.toChars(code.toInt(16)))
}
