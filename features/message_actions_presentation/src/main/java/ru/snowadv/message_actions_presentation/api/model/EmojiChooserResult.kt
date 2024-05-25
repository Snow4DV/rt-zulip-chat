package ru.snowadv.message_actions_presentation.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface EmojiChooserResult : Parcelable {
    @Parcelize
    data class EmojiWasChosen(val emojiName: String, val messageId: Long): EmojiChooserResult
}