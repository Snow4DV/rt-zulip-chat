package ru.snowadv.message_actions_presentation.action_chooser.ui.elm

import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserEffectElm
import ru.snowadv.message_actions_presentation.api.model.ActionChooserResult
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.elm.EmojiChooserEffectUiElm

internal sealed interface ActionChooserEffectUiElm {
    data class CopyMessageToClipboard(val content: String) : ActionChooserEffectUiElm
    data class CloseWithResult(val result: ActionChooserResult): ActionChooserEffectUiElm
    data class ShowError(val throwable: Throwable, val errorMessage: String?): ActionChooserEffectUiElm
}