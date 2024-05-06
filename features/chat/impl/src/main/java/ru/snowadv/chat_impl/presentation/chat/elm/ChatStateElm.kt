package ru.snowadv.chat_impl.presentation.chat.elm

import ru.snowadv.chat_impl.presentation.model.ChatMessage
import ru.snowadv.chat_impl.presentation.model.ChatPaginationStatus
import ru.snowadv.events_api.domain.model.EventQueueProperties
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.model.ScreenState

internal data class ChatStateElm(
    val sendingMessage: Boolean = false,
    val changingReaction: Boolean = false,
    val stream: String,
    val topic: String,
    val screenState: ScreenState<List<DelegateItem>> = ScreenState.Loading(),
    val messages: List<ChatMessage> = emptyList(),
    val messageField: String = "",
    val actionButtonType: ActionButtonType = ActionButtonType.ADD_ATTACHMENT,
    val paginationStatus: ChatPaginationStatus = ChatPaginationStatus.None,
    val eventQueueData: EventQueueProperties?,
    val resumed: Boolean = false,
) {

    val firstLoadedMessageId: Long? = messages.firstOrNull()?.id

    val isActionButtonVisible = !screenState.isLoading && !sendingMessage

    enum class ActionButtonType {
        SEND_MESSAGE,
        ADD_ATTACHMENT,
    }
}