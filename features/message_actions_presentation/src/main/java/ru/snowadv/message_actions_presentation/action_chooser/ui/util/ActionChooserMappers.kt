package ru.snowadv.message_actions_presentation.action_chooser.ui.util


import ru.snowadv.message_actions_presentation.action_chooser.presentation.model.MessageAction
import ru.snowadv.message_actions_presentation.action_chooser.ui.model.UiMessageAction
import ru.snowadv.utils.EmojiUtils
import ru.snowadv.chat_domain_api.model.ChatEmoji as DomainChatEmoji


internal object ActionChooserMappers {
    fun MessageAction.toUiMessageAction(): UiMessageAction {
        return when(this) {
            is MessageAction.AddReaction -> UiMessageAction.AddReaction(loading)
            is MessageAction.CopyMessage -> UiMessageAction.CopyMessage(loading)
            is MessageAction.EditMessage -> UiMessageAction.EditMessage(loading)
            is MessageAction.MoveMessage -> UiMessageAction.MoveMessage(loading)
            is MessageAction.OpenSenderProfile -> UiMessageAction.OpenSenderProfile(loading)
            is MessageAction.RemoveMessage -> UiMessageAction.RemoveMessage(loading)
            is MessageAction.ReloadMessage -> UiMessageAction.ReloadMessage(loading)
        }
    }

    fun UiMessageAction.toMessageAction(): MessageAction {
        return when(this) {
            is UiMessageAction.AddReaction -> MessageAction.AddReaction(loading)
            is UiMessageAction.CopyMessage -> MessageAction.CopyMessage(loading)
            is UiMessageAction.EditMessage -> MessageAction.EditMessage(loading)
            is UiMessageAction.MoveMessage -> MessageAction.MoveMessage(loading)
            is UiMessageAction.OpenSenderProfile -> MessageAction.OpenSenderProfile(loading)
            is UiMessageAction.RemoveMessage -> MessageAction.RemoveMessage(loading)
            is UiMessageAction.ReloadMessage -> MessageAction.ReloadMessage(loading)
        }
    }
}