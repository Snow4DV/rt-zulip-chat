package ru.snowadv.chat.presentation.chat.state

import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.model.ScreenState

data class ChatScreenState(
    val sendingMessage: Boolean = false,
    val changingReaction: Boolean = false,
    val stream: String,
    val topic: String,
    val screenState: ScreenState<List<DelegateItem>> = ScreenState.Loading,
    val messageField: String = "",
    val actionButtonType: ActionButtonType = ActionButtonType.ADD_ATTACHMENT,
) {

    val isActionButtonVisible = !screenState.isLoading && !sendingMessage

    enum class ActionButtonType {
        SEND_MESSAGE,
        ADD_ATTACHMENT,
    }
}