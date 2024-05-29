package ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm

internal sealed interface MessageTopicChangerEventElm {
    sealed interface Ui : MessageTopicChangerEventElm {
        data object Init : Ui
        data class ChangedTopic(val newTopic: String) : Ui
        data class ChangedNotifyNewTopic(val checked: Boolean) : Ui
        data class ChangedNotifyOldTopic(val checked: Boolean) : Ui
        data object OnRetryClicked : Ui
        data object OnMoveClicked : Ui
    }

    sealed interface Internal : MessageTopicChangerEventElm {
        data class LoadedTopics(val topics: List<String>) : Internal
        data object LoadingTopics : Internal
        data class LoadingTopicsError(val error: Throwable, val errorMessage: String?) : Internal

        data object MovingMessage : Internal
        data class MessageMoved(val newTopicName: String) : Internal
        data class MessageMovingError(val error: Throwable, val errorMessage: String?) : Internal
    }
}