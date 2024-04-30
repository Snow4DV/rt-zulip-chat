package ru.snowadv.chat_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.chat_api.presentation.ChatScreenFactory
import ru.snowadv.chat_impl.presentation.chat.elm.ChatActorElm
import ru.snowadv.chat_impl.presentation.chat.elm.ChatCommandElm
import ru.snowadv.chat_impl.presentation.chat.elm.ChatEventElm
import ru.snowadv.chat_impl.presentation.emoji_chooser.elm.EmojiChooserActorElm
import ru.snowadv.chat_impl.presentation.emoji_chooser.elm.EmojiChooserCommandElm
import ru.snowadv.chat_impl.presentation.emoji_chooser.elm.EmojiChooserEventElm
import ru.snowadv.chat_impl.presentation.feature.ChatScreenFactoryImpl
import vivid.money.elmslie.core.store.Actor

@Module
internal interface ChatFeatureModule {
    @Binds
    fun bindChatScreenFactoryImpl(chatScreenFactoryImpl: ChatScreenFactoryImpl): ChatScreenFactory
    @Binds
    fun bindChatActorElm(chatActorElm: ChatActorElm): Actor<ChatCommandElm, ChatEventElm>
    @Binds
    fun bindEmojiChooserActor(emojiChooserActorElm: EmojiChooserActorElm): Actor<EmojiChooserCommandElm, EmojiChooserEventElm>
    
}