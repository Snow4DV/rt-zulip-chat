package ru.snowadv.message_actions_presentation.action_chooser.ui.feature

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.Reusable
import ru.snowadv.message_actions_presentation.action_chooser.ui.ActionChooserBottomSheetDialog
import ru.snowadv.message_actions_presentation.api.screen_factory.ActionChooserDialogFactory
import javax.inject.Inject

@Reusable
internal class ActionChooserDialogFactoryImpl @Inject constructor() : ActionChooserDialogFactory {
    override fun create(
        resultKey: String,
        messageId: Long,
        senderUserId: Long,
    ): BottomSheetDialogFragment {
        return ActionChooserBottomSheetDialog.newInstance(
            resultKey = resultKey,
            messageId = messageId,
            userId = senderUserId
        )
    }
}