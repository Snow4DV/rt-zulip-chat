package ru.snowadv.message_actions_presentation.action_chooser.ui.model

import ru.snowadv.message_actions_presentation.R
import ru.snowadv.presentation.adapter.DelegateItem

internal sealed interface UiMessageAction : DelegateItem {
    val loading: Boolean
    val nameResId: Int
    val iconResId: Int
    data class AddReaction(override val loading: Boolean) : UiMessageAction {
        override val nameResId: Int = R.string.add_reaction_action
        override val iconResId: Int = R.drawable.ic_add_reaction_action
        override val id: Any = 1
    }
    data class RemoveMessage(override val loading: Boolean) : UiMessageAction {
        override val nameResId: Int = R.string.remove_message_action
        override val iconResId: Int = R.drawable.ic_remove_message_action
        override val id: Any = 2
    }
    data class EditMessage(override val loading: Boolean) : UiMessageAction {
        override val nameResId: Int = R.string.edit_message_action
        override val iconResId: Int = R.drawable.ic_edit_message_action
        override val id: Any = 3
    }
    data class MoveMessage(override val loading: Boolean) : UiMessageAction {
        override val nameResId: Int = R.string.move_message_action
        override val iconResId: Int = R.drawable.ic_change_topic_action
        override val id: Any = 4
    }
    data class CopyMessage(override val loading: Boolean) : UiMessageAction {
        override val nameResId: Int = R.string.copy_message_action
        override val iconResId: Int = R.drawable.ic_copy_message_content
        override val id: Any = 5
    }
    data class OpenSenderProfile(override val loading: Boolean) : UiMessageAction {
        override val nameResId: Int = R.string.open_profile_action
        override val iconResId: Int = R.drawable.ic_open_profie_action
        override val id: Any = 6
    }
    data class ReloadMessage(override val loading: Boolean) : UiMessageAction {
        override val nameResId: Int = R.string.reload_message
        override val iconResId: Int = R.drawable.ic_reload_message
        override val id: Any = 7
    }
}