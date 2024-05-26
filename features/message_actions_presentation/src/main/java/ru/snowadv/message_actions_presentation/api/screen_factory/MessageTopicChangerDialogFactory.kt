package ru.snowadv.message_actions_presentation.api.screen_factory

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface MessageTopicChangerDialogFactory {
    companion object {
        const val BUNDLE_RESULT_KEY = "message_topic_changer_result_key"
        const val TAG = "message_topic_changer_dialog"
    }
    fun create(resultKey: String, messageId: Long, streamId: Long, topicName: String): BottomSheetDialogFragment
}