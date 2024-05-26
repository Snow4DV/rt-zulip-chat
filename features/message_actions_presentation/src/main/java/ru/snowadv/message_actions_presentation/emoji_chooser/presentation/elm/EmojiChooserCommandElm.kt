package ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm

internal sealed interface EmojiChooserCommandElm {
    data class LoadEmojis(val excludeEmojisCodes: Set<String>) : EmojiChooserCommandElm
}