package ru.snowadv.chat_domain_impl.di

import dagger.Component
import ru.snowadv.chat_domain_api.di.ChatDomainAPI
import ru.snowadv.chat_domain_api.di.ChatDomainDependencies
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [ChatDomainDependencies::class], modules = [ChatDomainModule::class])
@PerScreen
internal interface ChatDomainComponent : ChatDomainAPI {
    companion object {
        fun initAndGet(deps: ChatDomainDependencies): ChatDomainComponent {
            return DaggerChatDomainComponent.builder()
                .chatDomainDependencies(deps)
                .build()
        }
    }
}