package ru.snowadv.message_actions_presentation.action_chooser.presentation.elm

internal sealed interface ActionChooserCommandElm {
    data class RemoveMessage(val messageId: Long) : ActionChooserCommandElm
    data class OpenProfile(val userId: Long) : ActionChooserCommandElm
    data class LoadRawMessageToCopy(val messageId: Long, val streamName: String) : ActionChooserCommandElm
}