package ru.snowadv.messages_data_impl.di

import dagger.Component
import ru.snowadv.messages_data_api.di.MessagesDataModuleAPI
import ru.snowadv.messages_data_api.di.MessagesDataModuleDependencies
import javax.inject.Singleton

@Singleton
@Component(dependencies = [MessagesDataModuleDependencies::class], modules = [MessagesDataModule::class])
internal interface MessagesDataModuleComponent : MessagesDataModuleAPI {
    companion object {
        fun initAndGet(deps: MessagesDataModuleDependencies): MessagesDataModuleComponent {
            return DaggerMessagesDataModuleComponent.builder()
                .messagesDataModuleDependencies(deps)
                .build()
        }
    }
}