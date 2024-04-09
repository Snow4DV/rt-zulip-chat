package ru.snowadv.emojis_data.model

data class  DataEmoji(
    val name: String,
    val code: Int,
) {
    fun getCodeString() = String(Character.toChars(code))
}