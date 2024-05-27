package ru.snowadv.chat_presentation.chat.ui.elm

import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEventElm
import ru.snowadv.model.InputStreamOpener

sealed interface ChatEventUiElm {
    data object Init : ChatEventUiElm
    data object Resumed : ChatEventUiElm
    data object Paused : ChatEventUiElm
    data object SendMessageAddAttachmentButtonClicked : ChatEventUiElm
    data class MessageLongClicked(val messageId: Long, val userId: Long) : ChatEventUiElm
    data class AddReactionClicked(val messageId: Long) : ChatEventUiElm
    data class AddChosenReaction(val messageId: Long, val reactionName: String) : ChatEventUiElm
    data class RemoveReaction(val messageId: Long, val reactionName: String) : ChatEventUiElm
    data class MessageFieldChanged(val text: String) : ChatEventUiElm
    data object GoBackClicked : ChatEventUiElm
    data object ReloadClicked : ChatEventUiElm
    data object PaginationLoadMore : ChatEventUiElm
    data object ScrolledToNTopMessages : ChatEventUiElm
    data object FileChoosingDismissed : ChatEventUiElm
    data class FileWasChosen(val mimeType: String?, val inputStreamOpener: InputStreamOpener, val extension: String?) : ChatEventUiElm
    data class ClickedOnTopic(val topicName: String) : ChatEventUiElm
    data object OnLeaveTopicClicked : ChatEventUiElm
    data class TopicChanged(val newTopic: String) : ChatEventUiElm
    data class EditMessageClicked(val messageId: Long) : ChatEventUiElm
    data class MoveMessageClicked(val messageId: Long) : ChatEventUiElm
    data class MessageMovedToNewTopic(val topicName: String) : ChatEventUiElm
}