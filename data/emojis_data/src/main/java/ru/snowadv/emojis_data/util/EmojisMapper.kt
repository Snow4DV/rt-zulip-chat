package ru.snowadv.emojis_data.util

import ru.snowadv.emojis_data.model.DataEmoji
import ru.snowadv.network.model.EmojisResponseDto

internal object EmojisMapper {
    fun EmojisResponseDto.toDataEmojiList(): List<DataEmoji> {
        return this.nameToCodepoint.entries.asSequence()
            .map { DataEmoji(it.key, it.value) }.toList()
    }
}