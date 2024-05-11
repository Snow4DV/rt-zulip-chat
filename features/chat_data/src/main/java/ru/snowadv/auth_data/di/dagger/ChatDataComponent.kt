package ru.snowadv.auth_data.di.dagger

import dagger.Component
import ru.snowadv.auth_data.di.holder.ChatDataAPI
import ru.snowadv.auth_data.di.holder.ChatDataDependencies
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [ChatDataDependencies::class], modules = [ChatDataModule::class])
@PerScreen
internal interface ChatDataComponent : ChatDataAPI {
    companion object {
        fun initAndGet(deps: ChatDataDependencies): ChatDataComponent {
            return DaggerChatDataComponent.builder()
                .chatDataDependencies(deps)
                .build()
        }
    }
}