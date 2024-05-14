package ru.snowadv.chat_impl.presentation.emoji_chooser.elm

import ru.snowadv.chat_impl.presentation.model.ChatEmoji

internal sealed interface EmojiChooserEventElm {
    sealed interface Ui : EmojiChooserEventElm {
        data object Init : Ui
        data class OnEmojiChosen(val emoji: ChatEmoji): Ui
        data object OnRetryClicked: Ui
    }

    sealed interface Internal : EmojiChooserEventElm {
        data object EmojiLoadError : Internal
        data object EmojiLoading : Internal
        data class LoadedEmojis(val emojis: List<ChatEmoji>) : Internal
    }
}