package ru.snowadv.message_actions_presentation.action_chooser.presentation.elm

import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.message_actions_presentation.action_chooser.presentation.model.MessageAction
import ru.snowadv.model.ScreenState

internal data class ActionChooserStateElm(
    val messageId: Long,
    val senderId: Long,
    val streamName: String,
    val actions: List<MessageAction>,
    val isOwner: Boolean,
)