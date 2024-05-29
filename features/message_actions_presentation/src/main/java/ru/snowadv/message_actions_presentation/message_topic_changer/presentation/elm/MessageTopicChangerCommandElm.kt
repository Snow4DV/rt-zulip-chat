package ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm

sealed interface MessageTopicChangerCommandElm {
    data class LoadTopics(val streamId: Long) : MessageTopicChangerCommandElm
    data class MoveMessageToOtherTopic(
        val messageId: Long,
        val newTopic: String,
        val notifyOldThread: Boolean,
        val notifyNewThread: Boolean,
    ) : MessageTopicChangerCommandElm
}