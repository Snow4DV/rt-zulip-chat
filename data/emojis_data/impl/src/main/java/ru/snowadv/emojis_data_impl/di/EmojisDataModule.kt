package ru.snowadv.emojis_data_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.emojis_data_api.model.EmojiDataRepository
import ru.snowadv.emojis_data_impl.EmojiDataRepositoryImpl

@Module
internal interface EmojisDataModule {
    @Binds
    fun bindEmojiDataRepoImpl(emojiDataRepositoryImpl: EmojiDataRepositoryImpl): EmojiDataRepository
}