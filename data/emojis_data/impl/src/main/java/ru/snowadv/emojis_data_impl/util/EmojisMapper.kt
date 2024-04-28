package ru.snowadv.emojis_data_impl.util

import ru.snowadv.emojis_data_api.model.model.DataEmoji
import ru.snowadv.network.model.EmojisResponseDto

internal object EmojisMapper {
    fun EmojisResponseDto.toDataEmojiList(): List<DataEmoji> {
        return this.nameToCodepoint.entries.asSequence()
            .map { DataEmoji(it.key, it.value) }.toList()
    }
}