package ru.snowadv.message_actions_presentation.emoji_chooser.ui.feature

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.Reusable
import ru.snowadv.message_actions_presentation.api.screen_factory.EmojiChooserDialogFactory
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.EmojiChooserBottomSheetDialog
import javax.inject.Inject

@Reusable
internal class EmojiChooserDialogFactoryImpl @Inject constructor() : EmojiChooserDialogFactory {
    override fun create(
        resultKey: String,
        messageId: Long,
        excludeEmojisCodes: List<String>,
    ): BottomSheetDialogFragment {
        return EmojiChooserBottomSheetDialog.newInstance(resultKey, messageId, excludeEmojisCodes.toTypedArray())
    }
}