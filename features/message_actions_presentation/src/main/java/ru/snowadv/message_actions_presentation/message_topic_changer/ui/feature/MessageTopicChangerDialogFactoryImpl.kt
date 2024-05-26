package ru.snowadv.message_actions_presentation.message_topic_changer.ui.feature

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.Reusable
import ru.snowadv.message_actions_presentation.api.screen_factory.MessageEditorDialogFactory
import ru.snowadv.message_actions_presentation.api.screen_factory.MessageTopicChangerDialogFactory
import ru.snowadv.message_actions_presentation.message_editor.ui.MessageEditorBottomSheetDialog
import ru.snowadv.message_actions_presentation.message_topic_changer.ui.MessageTopicChangerBottomSheetDialog
import javax.inject.Inject

@Reusable
internal class MessageTopicChangerDialogFactoryImpl @Inject constructor() : MessageTopicChangerDialogFactory {
    override fun create(
        resultKey: String,
        messageId: Long,
        streamId: Long,
        topicName: String
    ): BottomSheetDialogFragment {
        return MessageTopicChangerBottomSheetDialog.newInstance(
            resultKey = resultKey,
            messageId = messageId,
            streamId = streamId,
            topicName = topicName,
        )
    }

}