package ru.snowadv.message_actions_presentation.action_chooser.ui.elm

import ru.snowadv.message_actions_presentation.action_chooser.presentation.model.MessageAction
import ru.snowadv.message_actions_presentation.action_chooser.ui.model.UiMessageAction

internal data class ActionChooserStateUiElm(
    val messageId: Long,
    val senderId: Long,
    val streamName: String,
    val actions: List<UiMessageAction>,
)