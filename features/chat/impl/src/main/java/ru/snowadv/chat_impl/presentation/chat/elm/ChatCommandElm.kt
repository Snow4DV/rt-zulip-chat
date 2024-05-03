package ru.snowadv.chat_impl.presentation.chat.elm

import ru.snowadv.events_api.domain.model.EventQueueProperties

sealed interface ChatCommandElm { // commands to actor
    data class LoadInitialMessages(
        val streamName: String,
        val topicName: String,
    ) : ChatCommandElm

    data class LoadMoreMessages(
        val streamName: String,
        val topicName: String,
        val firstLoadedMessageId: Long?,
        val includeAnchor: Boolean,
    ) : ChatCommandElm

    data class ObserveEvents(
        val streamName: String,
        val topicName: String,
        val isRestart: Boolean,
        val queueProps: EventQueueProperties?
    ) :
        ChatCommandElm

    data object GoBack : ChatCommandElm
    data class GoToProfile(val profileId: Long) : ChatCommandElm
    data class SendMessage(val streamName: String, val topicName: String, val text: String) : ChatCommandElm
    data class AddChosenReaction(val messageId: Long, val reactionName: String) : ChatCommandElm
    data class RemoveReaction(val messageId: Long, val reactionName: String) : ChatCommandElm
}