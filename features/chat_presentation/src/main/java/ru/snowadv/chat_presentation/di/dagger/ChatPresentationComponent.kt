package ru.snowadv.chat_presentation.di.dagger

import dagger.Component
import ru.snowadv.chat_presentation.chat.ui.ChatFragmentRenderer
import ru.snowadv.chat_presentation.di.holder.ChatPresentationAPI
import ru.snowadv.chat_presentation.di.holder.ChatPresentationDependencies
import ru.snowadv.chat_presentation.chat.ui.ChatFragment
import ru.snowadv.chat_presentation.emoji_chooser.ui.EmojiChooserBottomSheetDialog
import ru.snowadv.chat_presentation.emoji_chooser.presentation.elm.EmojiChooserStoreFactoryElm
import ru.snowadv.chat_presentation.emoji_chooser.ui.EmojiChooserRenderer
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [ChatPresentationDependencies::class], modules = [ChatPresentationModule::class])
@PerScreen
internal interface ChatPresentationComponent : ChatPresentationAPI {

    fun inject(chatFragmentRenderer: ChatFragmentRenderer)
    fun inject(emojiChooserRenderer: EmojiChooserRenderer)
    fun inject(chatFragment: ChatFragment)
    fun inject(emojiChooserBottomSheetDialog: EmojiChooserBottomSheetDialog)

    companion object {
        fun initAndGet(deps: ChatPresentationDependencies): ChatPresentationComponent {
            return DaggerChatPresentationComponent.builder()
                .chatPresentationDependencies(deps)
                .build()
        }
    }
}