package ru.snowadv.chat_impl.presentation.emoji_chooser.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import ru.snowadv.chat_impl.domain.use_case.GetEmojisUseCase
import ru.snowadv.chat_impl.presentation.util.ChatMappers.toUiChatEmoji
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