package ru.snowadv.chat_presentation.emoji_chooser.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import ru.snowadv.chat_domain_api.use_case.GetEmojisUseCase
import ru.snowadv.chat_presentation.chat.ui.util.ChatMappers.toUiChatEmoji
import ru.snowadv.model.Resource
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

@Reusable
internal class EmojiChooserActorElm @Inject constructor(
    private val getEmojisUseCase: GetEmojisUseCase,
) : Actor<EmojiChooserCommandElm, EmojiChooserEventElm>() {
    override fun execute(command: EmojiChooserCommandElm): Flow<EmojiChooserEventElm> {
        return when (command) {
            is EmojiChooserCommandElm.LoadEmojis -> {
                getEmojisUseCase().map { res ->
                    when(res) {
                        is Resource.Error -> res.data?.let { data -> EmojiChooserEventElm.Internal.LoadedEmojis(
                            emojis = data.map { it.toUiChatEmoji() }
                        ) } ?: EmojiChooserEventElm.Internal.EmojiLoadError(res.throwable)
                        is Resource.Loading -> res.data?.let { data -> EmojiChooserEventElm.Internal.LoadedEmojis(
                            emojis = data.map { it.toUiChatEmoji() }
                        ) } ?: EmojiChooserEventElm.Internal.EmojiLoading
                        is Resource.Success -> EmojiChooserEventElm.Internal.LoadedEmojis(
                            emojis = res.data.map { it.toUiChatEmoji() }
                        )
                    }
                }
            }
        }
    }

}