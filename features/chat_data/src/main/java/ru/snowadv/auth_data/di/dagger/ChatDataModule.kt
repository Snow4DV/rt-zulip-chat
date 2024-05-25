package ru.snowadv.auth_data.di.dagger

import dagger.Binds
import dagger.Module
import ru.snowadv.auth_data.repository.EmojiRepositoryImpl
import ru.snowadv.auth_data.repository.MessageRepositoryImpl
import ru.snowadv.chat_domain_api.repository.EmojiRepository
import ru.snowadv.chat_domain_api.repository.MessageRepository

@Module
internal interface ChatDataModule {
    @Binds
    fun bindsMessageRepositoryImpl(impl: MessageRepositoryImpl): MessageRepository
    @Binds
    fun bindEmojiRepositoryImpl(impl: EmojiRepositoryImpl): EmojiRepository
}