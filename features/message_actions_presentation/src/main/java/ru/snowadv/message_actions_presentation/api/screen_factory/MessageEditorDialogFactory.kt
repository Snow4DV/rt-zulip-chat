package ru.snowadv.message_actions_presentation.api.screen_factory

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface MessageEditorDialogFactory {
    companion object {
        const val BUNDLE_RESULT_KEY = "message_editor_result_key"
        const val TAG = "message_editor_dialog"
    }
    fun create(resultKey: String, messageId: Long, streamName: String): BottomSheetDialogFragment
}