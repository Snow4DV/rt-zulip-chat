package ru.snowadv.emojis_data.util

import ru.snowadv.emojis_data.model.DataEmoji
import ru.snowadv.network.model.EmojisDto

private val hexAllowedChars = "1234567890ABCDEFabcdef".toSet()

internal fun EmojisDto.toDataEmojiList(): List<DataEmoji> {
    return this.nameToCodepoint.entries.asSequence()
        .filter { it.value.all { it in hexAllowedChars } }
        .map { DataEmoji(it.key, it.value.toInt(radix = 16)) }.toList()
}