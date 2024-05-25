package ru.snowadv.message_actions_presentation.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface MessageEditResult : Parcelable {
    @Parcelize
    data class EditedMessage(val messageId: Long): MessageEditResult
}