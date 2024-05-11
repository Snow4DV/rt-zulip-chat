package ru.snowadv.chat_presentation.emoji_chooser.elm

import ru.snowadv.chat_presentation.common.ui.model.ChatEmoji
import ru.snowadv.model.ScreenState

internal data class EmojiChooserStateElm(
    val screenState: ScreenState<List<ChatEmoji>>,
)