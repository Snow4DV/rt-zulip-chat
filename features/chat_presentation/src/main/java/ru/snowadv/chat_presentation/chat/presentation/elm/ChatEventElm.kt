package ru.snowadv.chat_presentation.chat.presentation.elm

import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.chat_domain_api.model.ChatPaginatedMessages
import ru.snowadv.events_api.model.EventInfoHolder
import ru.snowadv.events_api.model.EventSenderType
import ru.snowadv.model.InputStreamOpener

internal sealed interface ChatEventElm {

    sealed interface Ui : ChatEventElm {
        data object Init : Ui
        data object Resumed : Ui
        data object Paused : Ui
        data object SendMessageAddAttachmentButtonClicked : Ui
        data class MessageLongClicked(val messageId: Long, val userId: Long) : Ui
        data class AddReactionClicked(val messageId: Long) : Ui
        data class AddChosenReaction(val messageId: Long, val reactionName: String) : Ui
        data class RemoveReaction(val messageId: Long, val reactionName: String) : Ui
        data class GoToProfileClicked(val profileId: Long) : Ui
        data class MessageFieldChanged(val text: String) : Ui
        data object GoBackClicked : Ui
        data object ReloadClicked : Ui
        data object PaginationLoadMore : Ui
        data object ScrolledToNTopMessages : Ui
        data object FileChoosingDismissed : Ui
        data class FileWasChosen(val mimeType: String?, val inputStreamOpener: InputStreamOpener, val extension: String?) :
            Ui
    }

    sealed interface Internal : ChatEventElm {
        data class InitialChatLoaded(val messages: ChatPaginatedMessages, val cached: Boolean) :
            Internal
        data class MoreMessagesLoaded(val messages: ChatPaginatedMessages) : Internal

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
        data class SendingMessageError(val retryCommandElm: ChatCommandElm): Internal
        data class ChangingReactionError(val retryEvent: ChatEventElm) : Internal
        data object MessageSent : Internal
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
        }
    }
}