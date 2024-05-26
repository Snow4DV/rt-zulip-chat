package ru.snowadv.message_actions_presentation.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface MessageEditorResult : Parcelable {
    @Parcelize
    data class EditedMessage(val messageId: Long): MessageEditorResult
    @Parcelize
    data class Error(val errorMessage: String): MessageEditorResult
}