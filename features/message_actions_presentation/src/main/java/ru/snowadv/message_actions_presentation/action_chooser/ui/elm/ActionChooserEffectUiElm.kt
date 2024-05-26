package ru.snowadv.message_actions_presentation.action_chooser.ui.elm

import ru.snowadv.message_actions_presentation.api.model.ActionChooserResult

internal sealed interface ActionChooserEffectUiElm {
    data class CopyMessageToClipboard(val content: String) : ActionChooserEffectUiElm
    data class CloseWithResult(val result: ActionChooserResult): ActionChooserEffectUiElm
    data class FinishWithError(val throwable: Throwable, val errorMessage: String?): ActionChooserEffectUiElm
}