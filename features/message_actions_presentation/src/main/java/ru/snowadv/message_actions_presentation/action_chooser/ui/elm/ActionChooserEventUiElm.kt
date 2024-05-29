package ru.snowadv.message_actions_presentation.action_chooser.ui.elm

import ru.snowadv.message_actions_presentation.action_chooser.presentation.model.MessageAction
import ru.snowadv.message_actions_presentation.action_chooser.ui.model.UiMessageAction

internal sealed interface ActionChooserEventUiElm {
    data class OnActionChosen(val action: UiMessageAction) : ActionChooserEventUiElm
}