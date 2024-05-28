package ru.snowadv.chat_presentation.chat.presentation.elm

import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.chat_domain_api.model.ChatPaginationStatus
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.ScreenState

data class ChatStateElm(
    val sendingMessage: Boolean = false,
    val changingReaction: Boolean = false,
    val uploadingFile: Boolean = false,
    val stream: String,
    val topic: String,
    val screenState: ScreenState<List<ChatMessage>> = ScreenState.Loading(),
    val messages: List<ChatMessage> = emptyList(),
    val messageField: String = "",
    val actionButtonType: ActionButtonType = ActionButtonType.ADD_ATTACHMENT,
    val paginationStatus: ChatPaginationStatus = ChatPaginationStatus.None,
    val eventQueueData: EventQueueProperties?,
    val resumed: Boolean = false,
) {

    val firstLoadedMessageId: Long? = messages.firstOrNull()?.id

    val isActionButtonVisible = !screenState.isLoading && !sendingMessage && !uploadingFile

    enum class ActionButtonType {
        SEND_MESSAGE,
        ADD_ATTACHMENT,
    }
}