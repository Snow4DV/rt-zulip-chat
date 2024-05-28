package ru.snowadv.chat_presentation.chat.presentation.elm

import ru.snowadv.channels_domain_api.model.Topic
import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.chat_domain_api.model.ChatPaginatedMessages
import ru.snowadv.chat_presentation.chat.ui.elm.ChatEventUiElm
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventInfoHolder
import ru.snowadv.events_api.model.EventSenderType
import ru.snowadv.events_api.model.EventStreamUpdateFlagsMessages
import ru.snowadv.model.InputStreamOpener
import ru.snowadv.model.Resource

sealed interface ChatEventElm {

    sealed interface Ui : ChatEventElm {
        data object Init : Ui
        data object Resumed : Ui
        data object Paused : Ui
        data object SendMessageAddAttachmentButtonClicked : Ui
        data class MessageLongClicked(val messageId: Long, val userId: Long) : Ui
        data class AddReactionClicked(val messageId: Long) : Ui
        data class AddChosenReaction(val messageId: Long, val reactionName: String) : Ui
        data class RemoveReaction(val messageId: Long, val reactionName: String) : Ui
        data class MessageFieldChanged(val text: String) : Ui
        data class ClickedOnTopic(val topicName: String) : Ui
        data object GoBackClicked : Ui
        data object ReloadClicked : Ui
        data object PaginationLoadMore : Ui
        data object ScrolledToNTopMessages : Ui
        data object FileChoosingDismissed : Ui
        data object ClickedOnLeaveTopic : Ui
        data class FileWasChosen(val mimeType: String?, val inputStreamOpener: InputStreamOpener, val extension: String?) : Ui
        data class TopicChanged(val newTopic: String) : Ui
        data class EditMessageClicked(val messageId: Long) : Ui
        data class MoveMessageClicked(val messageId: Long) : Ui
        data class MessageMovedToNewTopic(val topicName: String) : Ui
        data class ReloadMessageClicked(val messageId: Long) : Ui
    }

    sealed interface Internal : ChatEventElm {
        data class TopicsResourceChanged(val topicsRes: Resource<List<String>>) : Internal
        data class InitialChatLoaded(val messages: ChatPaginatedMessages) : Internal
        data class InitialChatLoadedFromCache(val messages: ChatPaginatedMessages) : Internal
        data class MoreMessagesLoaded(val messages: ChatPaginatedMessages) : Internal

        data class LoadedMovedMessage(val message: ChatMessage, val queueId: String, val eventId: Long) : Internal
        data class ErrorFetchingMovedMessage(val reason: Throwable, val queueId: String, val eventId: Long) : Internal

        data class Error(val throwable: Throwable, val cachedMessages: ChatPaginatedMessages?) :
            Internal
        data object PaginationError : Internal

        data object FileUploaded : Internal
        data class UploadingFileError(val retryEvent: ChatEventElm) : Internal
        data object UploadingFile: Internal

        data object Loading : Internal
        data object PaginationLoading : Internal

        data object SendingMessage : Internal
        data object ChangingReaction : Internal
        data object SendingMessageError: Internal
        data class ChangingReactionError(val retryEvent: ChatEventElm) : Internal
        data class MessageSent(val destTopic: String) : Internal
        data object ReactionChanged : Internal

        sealed class ServerEvent : Internal, EventInfoHolder {
            data class EventQueueRegistered(
                override val queueId: String,
                override val eventId: Long,
                val timeoutSeconds: Int,
            ) : ServerEvent() {
                override val senderType: EventSenderType
                    get() = EventSenderType.SYNTHETIC_REGISTER
            }

            data class EventQueueFailed(
                override val queueId: String?,
                override val eventId: Long,
                val reason: Throwable,
                val recreateQueue: Boolean,
            ) : ServerEvent() {
                override val senderType: EventSenderType
                    get() = EventSenderType.SYNTHETIC_FAIL
            }


            data class EventQueueUpdated(
                override val queueId: String?,
                override val eventId: Long
            ) : ServerEvent()

            data class NewMessage(
                override val queueId: String?,
                override val eventId: Long,
                val message: ChatMessage,
            ) : ServerEvent()

            data class MessageDeleted(
                override val queueId: String?,
                override val eventId: Long,
                val messageId: Long,
            ) : ServerEvent()

            data class MessageUpdated(
                override val queueId: String?,
                override val eventId: Long,
                val messageId: Long,
                val newContent: String?,
                val newSubject: String?,
            ) : ServerEvent()

            data class ReactionAdded(
                override val queueId: String?,
                override val eventId: Long,
                val messageId: Long,
                val emoji: ChatEmoji,
                val currentUserReaction: Boolean,
            ) : ServerEvent()

            data class ReactionRemoved(
                override val queueId: String?,
                override val eventId: Long,
                val messageId: Long,
                val emoji: ChatEmoji,
                val currentUserReaction: Boolean,
            ) : ServerEvent()

            data class MessagesRead(
                override val queueId: String?,
                override val eventId: Long,
                val addFlagMessagesIds: List<Long>,
            ) : ServerEvent()

            data class MessagesUnread(
                override val queueId: String?,
                override val eventId: Long,
                val removeFlagMessagesIds: List<Long>,
            ) : ServerEvent()
        }
    }
}