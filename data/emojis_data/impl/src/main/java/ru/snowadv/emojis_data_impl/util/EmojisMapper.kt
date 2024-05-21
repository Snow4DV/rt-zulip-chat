package ru.snowadv.emojis_data_impl.util

import ru.snowadv.database.entity.EmojiEntity
import ru.snowadv.emojis_data_api.model.model.DataEmoji
import ru.snowadv.network.model.EmojisResponseDto

internal object EmojisMapper {
    fun EmojisResponseDto.toDataEmojiList(): List<DataEmoji> {
        return this.nameToCodepoint.entries.asSequence()
            .map { DataEmoji(name = it.key, code = it.value) }.toList()
    }

    fun EmojisResponseDto.toEntityEmojiList(): List<EmojiEntity> {
        return this.nameToCodepoint.entries.asSequence()
            .map { EmojiEntity(name = it.key, code = it.value) }.toList()
    }

    fun EmojiEntity.toDataEmoji(): DataEmoji {
        return DataEmoji(name = name, code = code)
    }
}