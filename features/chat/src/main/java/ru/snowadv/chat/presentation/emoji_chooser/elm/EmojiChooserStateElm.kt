package ru.snowadv.chat.presentation.emoji_chooser.elm

import ru.snowadv.chat.presentation.model.ChatEmoji
import ru.snowadv.presentation.model.ScreenState

data class EmojiChooserStateElm(
    val screenState: ScreenState<List<ChatEmoji>>,
)