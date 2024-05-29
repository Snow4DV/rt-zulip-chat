package ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm

import ru.snowadv.model.ScreenState

internal data class MessageTopicChangerStateElm(
    val messageId: Long,
    val streamId: Long,
    val currentTopicName: String,
    val topics: ScreenState<List<String>> = ScreenState.Loading(),
    val movingMessage: Boolean = false,
    val notifyNewTopic: Boolean = false,
    val notifyOldTopic: Boolean = false,
)