package ru.snowadv.chat_presentation.emoji_chooser.presentation.elm

internal sealed interface EmojiChooserCommandElm {
    data object LoadEmojis : EmojiChooserCommandElm
}