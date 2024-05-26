package ru.snowadv.message_actions_presentation.action_chooser.presentation.elm

import ru.snowadv.message_actions_presentation.api.model.ActionChooserResult


internal sealed interface ActionChooserEffectElm {
    data class CopyMessageToClipboard(val content: String) : ActionChooserEffectElm
    data class CloseWithResult(val result: ActionChooserResult): ActionChooserEffectElm
    data class FinishWithError(val throwable: Throwable, val errorMessage: String?): ActionChooserEffectElm
}