package ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm

import ru.snowadv.chat_domain_api.model.ChatEmoji

internal sealed interface EmojiChooserEventElm {
    sealed interface Ui : EmojiChooserEventElm {
        data object Init : Ui
        data class OnEmojiChosen(val emoji: ChatEmoji): Ui
        data object OnRetryClicked: Ui
    }

    sealed interface Internal : EmojiChooserEventElm {
        data class EmojiLoadError(val error: Throwable) : Internal
        data object EmojiLoading : Internal
        data class LoadedEmojis(val emojis: List<ChatEmoji>) : Internal
    }
}