package ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm

import ru.snowadv.chat_domain_api.model.ChatEmoji


internal sealed interface EmojiChooserEffectElm {
    data class CloseWithChosenEmoji(val emoji: ChatEmoji): EmojiChooserEffectElm
}