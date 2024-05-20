package ru.snowadv.chat_presentation.emoji_chooser.presentation.elm

import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.model.ScreenState

internal data class EmojiChooserStateElm(
    val screenState: ScreenState<List<ChatEmoji>>,
)