package ru.snowadv.message_actions_presentation.api.screen_factory

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.snowadv.message_actions_presentation.api.model.EmojiChooserResult

interface EmojiChooserDialogFactory {
    companion object {
        const val BUNDLE_RESULT_KEY = "emoji_chooser_result_key"
        const val TAG = "emoji_chooser_dialog"
    }
    fun create(resultKey: String, messageId: Long, excludeEmojis: List<String>): BottomSheetDialogFragment
}