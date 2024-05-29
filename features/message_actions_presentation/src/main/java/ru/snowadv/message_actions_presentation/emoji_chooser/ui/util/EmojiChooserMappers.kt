package ru.snowadv.message_actions_presentation.emoji_chooser.ui.util


import ru.snowadv.message_actions_presentation.emoji_chooser.ui.model.ChatEmoji
import ru.snowadv.utils.EmojiUtils
import ru.snowadv.chat_domain_api.model.ChatEmoji as DomainChatEmoji


internal object EmojiChooserMappers {
    fun DomainChatEmoji.toUiChatEmoji(): ChatEmoji {
        return ChatEmoji(
            name = name,
            code = code,
            convertedEmojiString = EmojiUtils.combinedHexToString(code)
        )
    }

    fun ChatEmoji.toDomainChatEmoji(): DomainChatEmoji {
        return DomainChatEmoji(
            name = name,
            code = code,
        )
    }

}