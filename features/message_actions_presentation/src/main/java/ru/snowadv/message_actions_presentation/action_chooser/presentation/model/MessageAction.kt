package ru.snowadv.message_actions_presentation.action_chooser.presentation.model

internal sealed interface MessageAction {
    val loading: Boolean
    fun with(loading: Boolean): MessageAction

    data class AddReaction(override val loading: Boolean = false) : MessageAction {
        override fun with(loading: Boolean): MessageAction = copy(loading = loading)
    }
    data class RemoveMessage(override val loading: Boolean = false) : MessageAction {
        override fun with(loading: Boolean): MessageAction = copy(loading = loading)
    }
    data class EditMessage(override val loading: Boolean = false) : MessageAction {
        override fun with(loading: Boolean): MessageAction = copy(loading = loading)
    }
    data class MoveMessage(override val loading: Boolean = false) : MessageAction {
        override fun with(loading: Boolean): MessageAction = copy(loading = loading)
    }
    data class CopyMessage(override val loading: Boolean = false) : MessageAction {
        override fun with(loading: Boolean): MessageAction = copy(loading = loading)
    }
    data class OpenSenderProfile(override val loading: Boolean = false) : MessageAction {
        override fun with(loading: Boolean): MessageAction = copy(loading = loading)
    }
}