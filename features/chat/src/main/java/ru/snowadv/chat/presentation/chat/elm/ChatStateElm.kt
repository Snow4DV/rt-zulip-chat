package ru.snowadv.chat.presentation.chat.elm

import ru.snowadv.chat.presentation.model.ChatEmoji
import ru.snowadv.chat.presentation.model.ChatMessage
import ru.snowadv.chat.presentation.model.ChatPaginationStatus
import ru.snowadv.chat.presentation.model.ChatReaction
import ru.snowadv.chat.presentation.util.AdapterUtils.mapToAdapterMessagesAndDates
import ru.snowadv.event_api.helper.EventQueueProperties
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.model.ScreenState

internal data class ChatStateElm(
    val sendingMessage: Boolean = false,
    val changingReaction: Boolean = false,
    val stream: String,
    val topic: String,
    val screenState: ScreenState<List<DelegateItem>> = ScreenState.Loading,
    val messages: List<ChatMessage> = emptyList(),
    val messageField: String = "",
    val actionButtonType: ActionButtonType = ActionButtonType.ADD_ATTACHMENT,
    val paginationStatus: ChatPaginationStatus = ChatPaginationStatus.HasMore,
    val eventQueueData: EventQueueProperties?,
) {

    val paginatedScreenState = screenState.map { messagesDelegates ->
        when (paginationStatus) {
            ChatPaginationStatus.None, ChatPaginationStatus.LoadedAll -> messagesDelegates
            ChatPaginationStatus.Loading, ChatPaginationStatus.HasMore, ChatPaginationStatus.Error -> buildList {
                add(paginationStatus)
                addAll(messagesDelegates)
            }
        }
    }

    val firstLoadedMessageId: Long? = messages.firstOrNull()?.id

    val isActionButtonVisible = !screenState.isLoading && !sendingMessage

    enum class ActionButtonType {
        SEND_MESSAGE,
        ADD_ATTACHMENT,
    }
}