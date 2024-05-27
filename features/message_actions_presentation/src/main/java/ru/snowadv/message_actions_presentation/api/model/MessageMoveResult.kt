package ru.snowadv.message_actions_presentation.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface MessageMoveResult : Parcelable {
    @Parcelize
    data class MovedMessage(val messageId: Long, val newTopic: String): MessageMoveResult
    @Parcelize
    data class Error(val errorMessage: String): MessageMoveResult
}