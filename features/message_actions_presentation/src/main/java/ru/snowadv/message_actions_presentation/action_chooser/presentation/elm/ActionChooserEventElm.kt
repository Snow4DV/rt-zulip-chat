package ru.snowadv.message_actions_presentation.action_chooser.presentation.elm

import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.message_actions_presentation.action_chooser.presentation.model.MessageAction
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserEventElm

internal sealed interface ActionChooserEventElm {
    sealed interface Ui : ActionChooserEventElm {
        data class OnActionChosen(val action: MessageAction) : Ui
    }

    sealed interface Internal : ActionChooserEventElm {
        data object MessageRemoving : Internal
        data object MessageRemoved : Internal
        data class MessageRemovalError(val error: Throwable, val errorMessage: String?) : Internal

        data object LoadingMessageToCopy : Internal
        data class CopyMessageLoaded(val content: String) : Internal
        data class LoadingCopyMessageError(val error: Throwable, val errorMessage: String?) : Internal

        data class OpenedProfile(val userId: Long) : Internal
    }
}