package ru.snowadv.chat.presentation.emoji_chooser.elm

internal sealed interface EmojiChooserCommandElm {
    data object LoadEmojis : EmojiChooserCommandElm
}