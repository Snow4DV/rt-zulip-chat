package ru.snowadv.chat_domain_api.di

import ru.snowadv.chat_domain_api.repository.EmojiRepository
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.chat_domain_api.use_case.AddReactionUseCase
import ru.snowadv.chat_domain_api.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.GetEmojisUseCase
import ru.snowadv.chat_domain_api.use_case.ListenToChatEventsUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.RemoveReactionUseCase
import ru.snowadv.chat_domain_api.use_case.SendFileUseCase
import ru.snowadv.chat_domain_api.use_case.SendMessageUseCase
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface ChatDomainAPI : BaseModuleAPI {
    val addReactionUseCase: AddReactionUseCase
    val getCurrentMessagesUseCase: GetCurrentMessagesUseCase
    val getEmojisUseCase: GetEmojisUseCase
    val listenToChatEventsUseCase: ListenToChatEventsUseCase
    val loadMoreMessagesUseCase: LoadMoreMessagesUseCase
    val removeReactionUseCase: RemoveReactionUseCase
    val sendFileUseCase: SendFileUseCase
    val sendMessageUseCase: SendMessageUseCase
}