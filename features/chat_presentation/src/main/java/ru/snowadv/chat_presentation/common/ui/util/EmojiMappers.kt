package ru.snowadv.chat_presentation.common.ui.util

import ru.snowadv.chat_domain_api.model.ChatEmoji as DomainChatEmoji
import ru.snowadv.chat_presentation.common.ui.model.ChatEmoji as UiChatEmoji
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.utils.EmojiUtils

internal object EmojiMappers {
    fun DomainChatEmoji.toUiChatEmoji(): UiChatEmoji {
        return UiChatEmoji(
            name = name,
            code = code,
            convertedEmojiString = EmojiUtils.combinedHexToString(code)
        )
    }

    fun UiChatEmoji.toDomainChatEmoji(): DomainChatEmoji {
        return DomainChatEmoji(
            name = name,
            code = code,
        )
    }


    fun DomainEvent.ReactionDomainEvent.toUiChatEmoji(): UiChatEmoji {
        return UiChatEmoji(
            name = emojiName,
            code = emojiCode,
            convertedEmojiString = EmojiUtils.combinedHexToString(emojiCode),
        )
    }

    fun DomainEvent.ReactionDomainEvent.toDomainChatEmoji(): DomainChatEmoji {
        return DomainChatEmoji(
            name = emojiName,
            code = emojiCode,
        )
    }
}