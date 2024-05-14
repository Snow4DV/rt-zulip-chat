package ru.snowadv.chat.presentation.emoji_chooser.elm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.chat.domain.use_case.AddReactionUseCase
import ru.snowadv.chat.domain.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat.domain.use_case.GetEmojisUseCase
import ru.snowadv.chat.domain.use_case.ListenToChatEventsUseCase
import ru.snowadv.chat.domain.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat.domain.use_case.RemoveReactionUseCase
import ru.snowadv.chat.domain.use_case.SendMessageUseCase
import ru.snowadv.chat.presentation.chat.elm.ChatCommandElm
import ru.snowadv.chat.presentation.chat.elm.ChatEventElm
import ru.snowadv.chat.presentation.util.ChatMappers.toUiChatEmoji
import ru.snowadv.model.Resource
import vivid.money.elmslie.core.store.Actor

internal class EmojiChooserActorElm(
    private val getEmojisUseCase: GetEmojisUseCase,
) : Actor<EmojiChooserCommandElm, EmojiChooserEventElm>() {
    override fun execute(command: EmojiChooserCommandElm): Flow<EmojiChooserEventElm> {
        return when (command) {
            is EmojiChooserCommandElm.LoadEmojis -> {
                getEmojisUseCase().map { res ->
                    when(res) {
                        is Resource.Error -> EmojiChooserEventElm.Internal.EmojiLoadError
                        Resource.Loading -> EmojiChooserEventElm.Internal.EmojiLoading
                        is Resource.Success -> EmojiChooserEventElm.Internal.LoadedEmojis(
                            emojis = res.data.map { it.toUiChatEmoji() }
                        )
                    }
                }
            }
        }
    }

}