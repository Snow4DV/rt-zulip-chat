package ru.snowadv.message_actions_presentation.emoji_chooser.ui.elm

import ru.snowadv.message_actions_presentation.emoji_chooser.ui.model.ChatEmoji
import ru.snowadv.model.ScreenState

internal data class EmojiChooserStateUiElm(
    val screenState: ScreenState<List<ChatEmoji>>,
)