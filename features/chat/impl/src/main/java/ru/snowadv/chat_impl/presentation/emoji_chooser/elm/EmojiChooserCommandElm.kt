package ru.snowadv.chat_impl.presentation.emoji_chooser.elm

internal sealed interface EmojiChooserCommandElm {
    data object LoadEmojis : EmojiChooserCommandElm
}