package ru.snowadv.message_actions_presentation.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ActionChooserResult : Parcelable {
    @Parcelize
    data class AddReaction(val messageId: Long): ActionChooserResult
    @Parcelize
    data class RemovedMessage(val messageId: Long): ActionChooserResult
    @Parcelize
    data class EditMessage(val messageId: Long): ActionChooserResult
    @Parcelize
    data class MoveMessage(val messageId: Long): ActionChooserResult
    @Parcelize
    data class CopiedMessage(val messageId: Long): ActionChooserResult
    @Parcelize
    data class OpenedProfile(val profileId: Long): ActionChooserResult
    @Parcelize
    data class Error(val errorMessage: String): ActionChooserResult
}