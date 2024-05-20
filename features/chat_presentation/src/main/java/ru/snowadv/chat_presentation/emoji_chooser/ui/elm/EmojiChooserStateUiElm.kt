package ru.snowadv.chat_presentation.emoji_chooser.ui.elm

import ru.snowadv.chat_presentation.common.ui.model.ChatEmoji
import ru.snowadv.model.ScreenState

internal data class EmojiChooserStateUiElm(
    val screenState: ScreenState<List<ChatEmoji>>,
)