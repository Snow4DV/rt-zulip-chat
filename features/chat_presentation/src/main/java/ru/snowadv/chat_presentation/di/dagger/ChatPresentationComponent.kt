package ru.snowadv.chat_presentation.di.dagger

import dagger.Component
import ru.snowadv.chat_presentation.chat.ui.ChatFragmentRenderer
import ru.snowadv.chat_presentation.di.holder.ChatPresentationAPI
import ru.snowadv.chat_presentation.di.holder.ChatPresentationDependencies
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatStoreFactoryElm
import ru.snowadv.chat_presentation.emoji_chooser.elm.EmojiChooserStoreFactoryElm
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [ChatPresentationDependencies::class], modules = [ChatPresentationModule::class])
@PerScreen
internal interface ChatPresentationComponent : ChatPresentationAPI {
    val chatStoreFactory: ChatStoreFactoryElm
    val emojiChooserStoreFactory: EmojiChooserStoreFactoryElm

    fun inject(chatFragmentRenderer: ChatFragmentRenderer)
    companion object {
        fun initAndGet(deps: ChatPresentationDependencies): ChatPresentationComponent {
            return DaggerChatPresentationComponent.builder()
                .chatPresentationDependencies(deps)
                .build()
        }
    }
}