package ru.snowadv.chat_presentation.chat.presentation.elm

import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.InputStreamOpener

sealed interface ChatCommandElm { // commands to actor
    data class LoadInitialMessages(
        val streamName: String,
        val topicName: String?,
    ) : ChatCommandElm

    data class LoadTopicsFromCurrentStream(
        val streamId: Long,
    ) : ChatCommandElm

    data class LoadMoreMessages(
        val streamName: String,
        val topicName: String?,
        val firstLoadedMessageId: Long?,
        val includeAnchor: Boolean,
    ) : ChatCommandElm

    data class LoadMovedMessage(
        val messageId: Long,
        val streamName: String,
        val requestQueueId: String,
        val requestEventId: Long,
    ) : ChatCommandElm

    data class ObserveEvents(
        val streamName: String,
        val topicName: String?,
        val isRestart: Boolean,
        val queueProps: EventQueueProperties?
    ) :
        ChatCommandElm

    data object GoBack : ChatCommandElm
    data class GoToProfile(val profileId: Long) : ChatCommandElm
    data class SendMessage(val streamName: String, val topicName: String, val text: String) :
        ChatCommandElm

    data class AddChosenReaction(val messageId: Long, val reactionName: String) : ChatCommandElm
    data class RemoveReaction(val messageId: Long, val reactionName: String) : ChatCommandElm
    data class AddAttachment(
        val streamName: String,
        val topicName: String,
        val mimeType: String?,
        val inputStreamOpener: InputStreamOpener,
        val extension: String?,
    ) : ChatCommandElm
}