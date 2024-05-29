package ru.snowadv.message_actions_presentation.message_editor.ui.feature

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.Reusable
import ru.snowadv.message_actions_presentation.action_chooser.ui.ActionChooserBottomSheetDialog
import ru.snowadv.message_actions_presentation.api.screen_factory.MessageEditorDialogFactory
import ru.snowadv.message_actions_presentation.message_editor.ui.MessageEditorBottomSheetDialog
import javax.inject.Inject

@Reusable
internal class MessageEditorDialogFactoryImpl @Inject constructor() : MessageEditorDialogFactory {
    override fun create(
        resultKey: String,
        messageId: Long,
        streamName: String,
    ): BottomSheetDialogFragment {
        return MessageEditorBottomSheetDialog.newInstance(
            resultKey = resultKey,
            messageId = messageId,
            streamName = streamName,
        )
    }
}