package ru.snowadv.auth_data.util

import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.database.entity.EmojiEntity
import ru.snowadv.network.model.EmojisResponseDto

internal object EmojisMapper {
    fun EmojisResponseDto.toDomainEmojiList(): List<ChatEmoji> {
        return this.nameToCodepoint.entries.asSequence()
            .map { ChatEmoji(name = it.key, code = it.value) }.toList()
    }

    fun EmojisResponseDto.toEntityEmojiList(): List<EmojiEntity> {
        return this.nameToCodepoint.entries.asSequence()
            .map { EmojiEntity(name = it.key, code = it.value) }.toList()
    }

    fun EmojiEntity.toDomainEmoji(): ChatEmoji {
        return ChatEmoji(name = name, code = code)
    }
}
