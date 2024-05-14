package ru.snowadv.chat_presentation.chat.ui.elm

import ru.snowadv.chat_presentation.R
import ru.snowadv.chat_presentation.chat.ui.model.ChatMessage
import ru.snowadv.chat_presentation.chat.ui.model.ChatPaginationStatus
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.model.ScreenState

internal data class ChatStateUiElm(
    val sendingMessage: Boolean = false,
    val changingReaction: Boolean = false,
    val uploadingFile: Boolean = false,
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

    val isActionButtonVisible = !screenState.isLoading && !sendingMessage && !uploadingFile

    enum class ActionButtonType (val buttonResId: Int, val hintTextResId: Int) {
        SEND_MESSAGE(R.drawable.ic_send, R.string.send_message_hint),
        ADD_ATTACHMENT(R.drawable.ic_add_attachment, R.string.add_attachment_hint),
    }
}