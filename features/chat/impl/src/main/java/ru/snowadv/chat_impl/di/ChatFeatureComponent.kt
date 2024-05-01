package ru.snowadv.chat_impl.di

import dagger.Component
import ru.snowadv.chat_api.di.ChatFeatureAPI
import ru.snowadv.chat_api.di.ChatFeatureDependencies
import ru.snowadv.chat_impl.presentation.chat.ChatFragmentRenderer
import ru.snowadv.chat_impl.presentation.chat.elm.ChatStoreFactoryElm
import ru.snowadv.chat_impl.presentation.emoji_chooser.elm.EmojiChooserStoreFactoryElm
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [ChatFeatureDependencies::class], modules = [ChatFeatureModule::class])
@PerScreen
internal interface ChatFeatureComponent : ChatFeatureAPI {
    val chatStoreFactory: ChatStoreFactoryElm
    val emojiChooserStoreFactory: EmojiChooserStoreFactoryElm
    fun inject(renderer: ChatFragmentRenderer)
    companion object {
        fun initAndGet(deps: ChatFeatureDependencies): ChatFeatureComponent {
            return DaggerChatFeatureComponent.builder()
                .chatFeatureDependencies(deps)
                .build()
        }
    }
}