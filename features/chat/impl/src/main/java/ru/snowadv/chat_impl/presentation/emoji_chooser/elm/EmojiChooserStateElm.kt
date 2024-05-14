package ru.snowadv.chat_impl.presentation.emoji_chooser.elm

import ru.snowadv.chat_impl.presentation.model.ChatEmoji
import ru.snowadv.presentation.model.ScreenState

internal data class EmojiChooserStateElm(
    val screenState: ScreenState<List<ChatEmoji>>,
)