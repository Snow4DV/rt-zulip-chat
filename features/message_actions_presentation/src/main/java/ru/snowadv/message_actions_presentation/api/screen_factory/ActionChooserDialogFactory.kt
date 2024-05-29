package ru.snowadv.message_actions_presentation.api.screen_factory

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.snowadv.message_actions_presentation.action_chooser.ui.ActionChooserBottomSheetDialog
import ru.snowadv.message_actions_presentation.api.model.EmojiChooserResult

interface ActionChooserDialogFactory {
    companion object {
        const val BUNDLE_RESULT_KEY = "chooser_result"
        const val TAG = "action_chooser_dialog"
    }
    fun create(resultKey: String, messageId: Long, senderUserId: Long, streamName: String, isOwner: Boolean): BottomSheetDialogFragment
}