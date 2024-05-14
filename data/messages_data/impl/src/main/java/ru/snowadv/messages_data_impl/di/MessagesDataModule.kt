package ru.snowadv.messages_data_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.messages_data_api.MessageDataRepository
import ru.snowadv.messages_data_impl.MessageDataRepositoryImpl

@Module
internal interface MessagesDataModule {
    @Binds
    fun bindMessageDataRepositoryImpl(messageDataRepositoryImpl: MessageDataRepositoryImpl): MessageDataRepository
}