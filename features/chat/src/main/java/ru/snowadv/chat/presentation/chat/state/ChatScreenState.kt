package ru.snowadv.chat.presentation.chat.state

import ru.snowadv.chat.presentation.model.ChatMessage
import ru.snowadv.chat.presentation.util.mapToAdapterMessagesAndDates
import ru.snowadv.model.Resource
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.model.ScreenState

internal data class ChatScreenState(
    val sendingMessage: Boolean = false,
    val changingReaction: Boolean = false,
    val stream: String,
    val topic: String,
    val screenState: ScreenState<List<DelegateItem>> = ScreenState.Loading,
    val messages: List<ChatMessage> = emptyList(),
    val messageField: String = "",
    val actionButtonType: ActionButtonType = ActionButtonType.ADD_ATTACHMENT,
) {

    val isActionButtonVisible = !screenState.isLoading && !sendingMessage

    enum class ActionButtonType {
        SEND_MESSAGE,
        ADD_ATTACHMENT,
    }

    fun replaceOrAddMessages(newMessages: List<ChatMessage>): ChatScreenState {
        // messages can only be listened after initial fetch
        if (screenState is ScreenState.Error || screenState is ScreenState.Loading) return this

        val resultMessageList = buildMap {
            messages.forEach { this[it.id] = it }
            newMessages.forEach { this[it.id] = it }
        }.values.sortedBy { it.sentAt }

        return copy(
            screenState = ScreenState.Success(resultMessageList.mapToAdapterMessagesAndDates()),
            messages = resultMessageList,
        )
    }
}