package ru.snowadv.chat_presentation.chat.presentation.elm

import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.chat_domain_api.model.ChatPaginationStatus
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.Resource
import ru.snowadv.model.ScreenState

data class ChatStateElm(
    val sendingMessage: Boolean = false,
    val changingReaction: Boolean = false,
    val uploadingFile: Boolean = false,
    val streamId: Long, // needed to get topics
    val stream: String,
    val topic: String?, // can be null to get messages from all topics
    val screenState: ScreenState<List<ChatMessage>> = ScreenState.Loading(),
    val messages: List<ChatMessage> = emptyList(),
    val messageField: String = "",
    val actionButtonType: ActionButtonType = ActionButtonType.ADD_ATTACHMENT,
    val paginationStatus: ChatPaginationStatus = ChatPaginationStatus.None,
    val eventQueueData: EventQueueProperties?,
    val resumed: Boolean = false,
    val sendTopic: String,
    val topics: Resource<List<String>> = Resource.Loading(),
) {

    val firstLoadedMessageId: Long? = messages.firstOrNull()?.id

    val isLoading: Boolean = sendingMessage || changingReaction || uploadingFile
            || (screenState is ScreenState.Loading && screenState.data != null)

    enum class ActionButtonType {
        SEND_MESSAGE,
        ADD_ATTACHMENT,
    }
}